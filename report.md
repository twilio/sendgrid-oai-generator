# Supply Chain Security Scan — sendgrid-oai-generator

**Verdict: AT RISK**

The project has three active supply-chain exposure points under a locked-down build regime: (1) `examples/java/Dockerfile` pulls a public `openjdk:8` image from Docker Hub and clones `sendgrid-java` directly from GitHub at build time, both of which bypass any Artifactory pull-through cache; (2) the `pom.xml` declares no `<repositories>` or `<mirrors>` block, so Maven resolves all dependencies from the default Maven Central — this will silently fail or pull from the internet if a repo-level `settings.xml` mirror is not injected at build time; and (3) the Buildkite/CI layer is absent from the repo, so there is no evidence of registry override env vars or allowlisted egress for Docker-in-Docker builds.

---

| # | Area | Severity | File:Line | Finding | Recommended Fix | Owner Hint |
|---|------|----------|-----------|---------|-----------------|------------|
| 1 | Docker — base image | CRITICAL | `examples/java/Dockerfile:1` | `FROM openjdk:8` pulls from Docker Hub (`registry-1.docker.io`) with no registry prefix. The `openjdk:8` tag is also EOL/unmaintained. Under the locked-down regime Docker Hub is blocked without an Artifactory mirror prefix. | Replace with `FROM <artifactory-host>/docker-remote/openjdk:8` (or migrate to `eclipse-temurin:8-jdk` via Artifactory mirror). | Phase 2 — Docker registry mirror |
| 2 | Docker — runtime git clone | CRITICAL | `examples/java/Dockerfile:6` | `RUN git clone https://github.com/sendgrid/sendgrid-java.git` fetches an unpinned HEAD of a public GitHub repo at image build time. No SHA or tag is specified, so the content is not reproducible and any compromise of that repo is pulled in silently. | Pin to a specific commit SHA: `git clone --depth 1 --branch <TAG> https://github.com/sendgrid/sendgrid-java.git && cd sendgrid-java && git checkout <COMMIT_SHA>`. Long-term: mirror the repo to internal git or vendor the source. | Phase 3 — egress allowlist / Phase 6 — repo-level pinning |
| 3 | Docker — apt-get without pinned sources | HIGH | `examples/java/Dockerfile:3` | `apt-get install maven -y` pulls from the default Debian/Ubuntu apt mirror (`archive.ubuntu.com` or Debian CDN). No apt source override or version pin is present. Blocked if apt egress is restricted; also non-reproducible. | Use an Artifactory apt proxy or pre-install Maven in the base image. Pin the package version: `apt-get install maven=3.x.x -y`. | Phase 2 — apt proxy |
| 4 | Docker — prism image from Docker Hub | HIGH | `examples/prism/docker-compose.yml:12` | `image: stoplight/prism:4.10.3` pulls directly from Docker Hub. `docker compose build --pull` in `prism.sh:11` forces a re-pull on every run. | Replace with `<artifactory-host>/docker-remote/stoplight/prism:4.10.3` and remove `--pull` or mirror the image internally. | Phase 2 — Docker registry mirror |
| 5 | Maven — no explicit repository/mirror config | HIGH | `pom.xml` (no `<repositories>` block) | `pom.xml` contains no `<repositories>` or `<distributionManagement>` block, so Maven falls back to Maven Central (`repo1.maven.org`). Under the locked-down regime, builds will fail or bypass Artifactory unless a `settings.xml` mirror is injected by the CI environment. There is no evidence of a committed `settings.xml`. | Commit a `settings.xml` (or `.mvn/settings.xml`) that points all repositories to the internal Artifactory Maven proxy. Add `<repository>` blocks in `pom.xml` explicitly pointing to Artifactory. | Phase 2 — Maven/Artifactory cache |
| 6 | Maven — unpinned `openapi-generator-cli` version pattern | MEDIUM | `pom.xml:141` | `<openapi-generator-version>7.4.0</openapi-generator-version>` is a release version (not SNAPSHOT/LATEST), so this is adequately pinned for the artifact itself. However, the version is resolved via Maven Central (see finding #5) — the pin is only safe if the Artifactory mirror is enforced. No checksum/hash verification is present. | Acceptable once the Artifactory mirror is in place. Optionally add a `<checksums>` verification step or use `mvn dependency:go-offline` in CI to lock the dependency graph. | Phase 2 — Maven/Artifactory cache |
| 7 | Git transport — SSH remote | MEDIUM | `.git/config:7` | The repo's own `origin` remote uses `git@github.com:twilio/sendgrid-oai-generator.git` (SSH transport). If CI agents run in a network segment where outbound SSH (port 22) to GitHub is blocked post-gate, clones and pushes will fail. The `git clone https://github.com/sendgrid/sendgrid-java.git` in the Dockerfile uses HTTPS, which is an inconsistency. | Standardise on HTTPS for CI: `git remote set-url origin https://github.com/twilio/sendgrid-oai-generator.git`. Confirm the Buildkite agent has an SSH key or a HTTPS token injected. | Phase 3 — git transport |
| 8 | CI/Buildkite — no pipeline config in repo | MEDIUM | `.buildkite/` (absent) | No `.buildkite/*.yml` pipeline file is committed. It is unknown whether the pipeline that triggers this repo's builds has `DOCKER_REGISTRY`, `NPM_REGISTRY`, or `MAVEN_MIRROR_URL` env vars set, or uses a Buildkite queue with restricted egress. Without the pipeline file, compliance cannot be verified in-repo. | Commit a `.buildkite/pipeline.yml` that sets registry override env vars and targets the approved Buildkite queue. At minimum document which external pipeline definition controls this repo's builds. | Phase 3 — agent env vars / Phase 4 — queue gate |
| 9 | Python dependency — `pystache` undeclared | MEDIUM | `scripts/build_sendgrid_library.py:8` | `import pystache` is used in `build_sendgrid_library.py` but there is no `requirements.txt`, `pyproject.toml`, or `setup.cfg` declaring it. The dependency must be installed manually (`pip install pystache`), which means it is pulled from PyPI at operator/CI discretion with no version pin or hash. `pystache` is also effectively unmaintained (last release 2012–2020). | Add a `requirements.txt` (or `pyproject.toml`) pinning `pystache==0.6.5` (or the version in use) with a hash: `pystache==0.6.5 --hash=sha256:...`. Consider replacing with the maintained `chevron` or `pystache2` fork. | Phase 2 — PyPI cache / Phase 6 — repo-level |
| 10 | Egress — GitHub clone inside Docker build | HIGH | `examples/java/Dockerfile:6` | (Overlaps with finding #2.) `git clone https://github.com/sendgrid/sendgrid-java.git` executes as a `RUN` step, meaning egress to `github.com` is required at container build time. If the Docker build agent has restricted egress (Artifactory-only), this will break. | Pre-stage the `sendgrid-java` source: either `COPY` it in as a build context, fetch it in the CI step before the Docker build, or cache it in an internal git mirror. | Phase 3 — egress allowlist |
| 11 | Egress — `docker compose build --pull` | MEDIUM | `scripts/prism.sh:11` | `--pull` flag forces Docker to re-pull base images on every `prism.sh` invocation, bypassing any local image cache. Under a restricted egress regime, any image not available in the Artifactory Docker mirror will cause a hard failure. | Remove `--pull` from the compose build command, or ensure all referenced images are mirrored in Artifactory before enabling this flag in CI. | Phase 2 — Docker registry mirror |
| 12 | Package registries — npm/PyPI/Go/Cargo/Terraform | INFO | N/A | No `package.json`, `.npmrc`, `yarn.lock`, `go.mod`, `Cargo.toml`, `Gemfile`, `terraform` files, or `pip.conf` found in the repository. No npm, PyPI (beyond undeclared pystache), Go proxy, Cargo, or Terraform registry references to audit. | No issues found in npm/Go/Cargo/Terraform/Gemfile registries. | N/A |
| 13 | GitHub Actions / CircleCI | INFO | N/A | No `.github/workflows/` or `.circleci/` directory found. No GitHub Actions or CircleCI configs to audit. | No issues found in GitHub Actions or CircleCI configs. | N/A |
| 14 | Git submodules | INFO | N/A | No `.gitmodules` file present. No submodule SSH/HTTPS transport to audit. | No issues found in git submodules. | N/A |
| 15 | Dependency hygiene — Maven deps | INFO | `pom.xml:110–138` | All Maven dependencies use explicit release versions (no `SNAPSHOT`, `LATEST`, or version ranges). `openapi-generator-cli:7.4.0`, `lombok:1.18.30`, `junit:4.13.2`, `slf4j-api:2.0.13`, `logback-classic:1.5.6` are all pinned. No postinstall scripts. Dependency count is small and all artifacts are from well-known, high-maturity projects. | No typosquatting or low-maturity signals. Versions are adequately pinned. | N/A |

---

## Quick Fix Script

### 1. Fix `examples/java/Dockerfile` — replace Docker Hub base image and pin git clone

```bash
# Replace openjdk:8 with Artifactory-mirrored image
# (substitute <ARTIFACTORY_HOST> with your internal Artifactory FQDN)
ARTIFACTORY_HOST="artifactory.example.com"

sed -i.bak \
  "s|FROM openjdk:8|FROM ${ARTIFACTORY_HOST}/docker-remote/eclipse-temurin:8-jdk|" \
  examples/java/Dockerfile

# Pin the git clone to a specific tag/SHA (replace <TAG> and <SHA> with actuals)
# Manual edit required — automated patch:
cat > /tmp/dockerfile.patch << 'EOF'
--- a/examples/java/Dockerfile
+++ b/examples/java/Dockerfile
@@ -3,7 +3,10 @@
 RUN apt-get update && apt-get install maven -y

 WORKDIR /app
-RUN git clone https://github.com/sendgrid/sendgrid-java.git
+RUN git clone --depth 1 --branch 4.x.x https://github.com/sendgrid/sendgrid-java.git \
+    && cd sendgrid-java \
+    && git checkout <COMMIT_SHA>
 WORKDIR /app/sendgrid-java/
EOF
# Apply: git apply /tmp/dockerfile.patch
```

### 2. Fix `examples/prism/docker-compose.yml` — use Artifactory-mirrored prism image

```bash
ARTIFACTORY_HOST="artifactory.example.com"

sed -i.bak \
  "s|image: stoplight/prism:4.10.3|image: ${ARTIFACTORY_HOST}/docker-remote/stoplight/prism:4.10.3|" \
  examples/prism/docker-compose.yml
```

### 3. Remove `--pull` from `scripts/prism.sh`

```bash
sed -i.bak \
  's/docker compose build --pull/docker compose build/' \
  scripts/prism.sh
```

### 4. Add Maven `settings.xml` pointing to Artifactory

```bash
# Create .mvn/settings.xml (Maven picks this up automatically when run from project root)
mkdir -p .mvn
cat > .mvn/settings.xml << 'EOF'
<settings>
  <mirrors>
    <mirror>
      <id>artifactory-central</id>
      <mirrorOf>*</mirrorOf>
      <url>https://artifactory.example.com/artifactory/maven-virtual</url>
    </mirror>
  </mirrors>
</settings>
EOF
```

### 5. Add `requirements.txt` for Python script deps

```bash
cat > scripts/requirements.txt << 'EOF'
pystache==0.6.5
EOF
# Then pin with hash:
# pip download pystache==0.6.5 -d /tmp/pkgs
# pip hash /tmp/pkgs/pystache-0.6.5.tar.gz
# Add --hash=sha256:<hash> to the requirements line
```

### 6. Switch git remote from SSH to HTTPS (for CI agents without SSH keys)

```bash
git remote set-url origin https://github.com/twilio/sendgrid-oai-generator.git
```
