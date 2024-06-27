import argparse
import json
import os
import re
import shutil
from pathlib import Path
from typing import List, Tuple

from clean_unused_imports import remove_unused_imports, remove_duplicate_imports

'''
Subdirectories map for maintaining directory
structures specific to language style.
'''
subdirectories = {
    'csharp': 'Rest',
    'php': 'Rest'
}
CLEANUP_IMPORT_LANGUAGES = ['java', 'php']
REMOVE_DUPLICATE_IMPORT_LANGUAGES = ['node']
CONFIG_FOLDER = 'tmp'


def build(openapi_spec_path: str, output_path: str, language: str) -> None:
    if os.path.isfile(openapi_spec_path):
        spec_folder, domain = os.path.split(openapi_spec_path)
        spec_files = [domain]
    else:
        spec_folder = openapi_spec_path
        spec_files = sorted(os.listdir(spec_folder))

    generate(spec_folder, spec_files, output_path, language)


def generate(spec_folder: str, spec_files: List[str], output_path: str, language: str) -> None:
    parent_dir = Path(__file__).parent.parent
    config_path = os.path.join(parent_dir, CONFIG_FOLDER, language)

    shutil.rmtree(config_path, ignore_errors=True)
    Path(config_path).mkdir(parents=True, exist_ok=True)

    for spec_file in spec_files:
        full_path = os.path.join(spec_folder, spec_file)
        full_config_path = os.path.join(config_path, spec_file)
        config = {
            'generatorName': f'sendgrid-{language}',
            'inputSpec': full_path,
            'outputDir': output_path,
            'inlineSchemaNameDefaults': {
                'arrayItemSuffix': ''
            },
        }

        with open(full_config_path, 'w') as f:
            f.write(json.dumps(config))

    print(f'Generating {output_path} from {spec_folder}')
    run_openapi_generator(parent_dir, language)
    print(f'Code generation completed at {output_path}')

    if language in CLEANUP_IMPORT_LANGUAGES:
        remove_unused_imports(output_path, language)
    if language in REMOVE_DUPLICATE_IMPORT_LANGUAGES:
        remove_duplicate_imports(output_path, language)


def run_openapi_generator(parent_dir: Path, language: str) -> None:
    properties = '-DapiTests=false'
    if language in {'node', 'python'}:
        properties += ' -DskipFormModel=false'

    command = f'cd {parent_dir} && java {properties} ' \
              f'-cp target/sendgrid-openapi-generator.jar ' \
              f'org.openapitools.codegen.OpenAPIGenerator batch {CONFIG_FOLDER}/{language}/*'

    if os.system(command + '> /dev/null') != 0:  # Suppress stdout
        raise RuntimeError()


def get_domain_info(oai_spec_location: str, domain: str, is_file: bool = False) -> Tuple[str, str, str]:
    full_path = oai_spec_location if is_file else os.path.join(oai_spec_location, domain)
    parts = re.split(r'tsg_(.+?)_?(v\d+)?\.', domain, flags=re.IGNORECASE)
    domain_name = parts[1]
    api_version = parts[2] or ''
    # This has to be removed when naming is made consistent across all languages
    if api_version == '':
        index = domain_name.find('_')
        if index != -1:
            domain_parts = re.split(r'(.+)_(.+)', domain_name, flags=re.IGNORECASE)
            domain_name = domain_parts[1]
            api_version = domain_parts[2]
    return full_path, domain_name, api_version


if __name__ == '__main__':
    example_text = '''example:

     python3 scripts/build_sendgrid_library.py /path/to/sendgrid-oai/spec/yaml /path/to/sendgrid-java -l java
     python3 scripts/build_sendgrid_library.py /path/to/sendgrid-oai/spec/yaml/tsg_alerts_v3.yaml /path/to/sendgrid-java -l java'''
    parser = argparse.ArgumentParser(description='Generate code from sendgrid-oai-generator', epilog=example_text,
                                     formatter_class=argparse.RawTextHelpFormatter)
    parser.add_argument('spec_path', type=str, help='path to open api specs')
    parser.add_argument('output_path', type=str, help='path to output the generated code')
    parser.add_argument('-l', '--lang', type=str, help='generate Sendgrid library from sendgrid-oai',
                        choices=['go', 'java', 'node', 'csharp', 'php', 'python', 'ruby'], required=True)
    args = parser.parse_args()
    build(args.spec_path, args.output_path, args.lang)
