#!/bin/bash
set -e

function should-generate() {
  [ "$LANGUAGES" = "" ] || [[ "$LANGUAGES" == *$1* ]]
}

function generate() {
  find -E "$OUT_DIR"/*/* ! -name "*_test.go" ! -regex "$OUT_DIR/[^/]+/__init__.py" -type f -delete || true
  files_regex=("examples/spec/*.yaml")

  # shellcheck disable=SC2161
  if [ "$1" = "sendgrid-java" ]; then
    files_regex=("examples/spec/*")
  fi

  rm -rf tmp
  mkdir -p tmp
  for api_spec in $files_regex; do
    echo "generatorName: $1
inputSpec: $api_spec
outputDir: $OUT_DIR
inlineSchemaNameDefaults:
  arrayItemSuffix: ''
additionalProperties:
  toggles: ./src/test/resources/config/test_toggles.json" >> tmp/"$(basename "$api_spec")"
  done

  java -DapiTests=false -DapiDocs=false $2 \
       -cp target/sendgrid-openapi-generator.jar \
       org.openapitools.codegen.OpenAPIGenerator batch tmp/*
}

function docker-run() {
  pushd "$(dirname "$1")"
  docker run \
    -v "${PWD}":/local \
    "$(docker build -f "$(basename "$1")" -q .)"
  popd
}

if should-generate java; then
  OUT_DIR=examples/java/src/main/java
  generate sendgrid-java
fi
