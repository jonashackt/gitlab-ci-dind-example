Example app for building inside GitLab CI - using Docker-in-Docker
=============================

This GitLab CI example needs a corresponding GitLab runner configuration - see https://github.com/jonashackt/gitlab-ci-stack#configure-a-docker-in-docker-enabled-gitlab-runner-with-the-docker-executor

See [.gitlab-ci.yml](.gitlab-ci.yml):

```
# This .gitlab-ci.yml is an extension of the example provided in: 
# https://github.com/jonashackt/restexamples/blob/master/.gitlab-ci.yml
# We use Docker in Docker here with a docker executor instead of the shell one
# --> Pinning the right Docker version for the service
image: docker:19.03.1

# Pinning the right Docker version for the service also
services:
  - docker:19.03.1-dind

variables:
  # see https://docs.gitlab.com/ee/ci/docker/using_docker_build.html#tls-enabled for Dind configuration
  # DOCKER_HOST: tcp://docker:2375 --> this should only be configured when using Kubernetes runners
  # When using dind, it's wise to use the overlayfs driver for
  # improved performance.
  DOCKER_DRIVER: overlay2
  # Specify to Docker where to create the certificates, Docker will
  # create them automatically on boot, and will create
  # `/certs/client` that will be shared between the service and job
  # container, thanks to volume mount from config.toml
  DOCKER_TLS_CERTDIR: "/certs"
  # see usage of Namespaces at https://docs.gitlab.com/ee/user/group/#namespaces
  REGISTRY_GROUP_PROJECT: $CI_REGISTRY/root/gitlab-ci-dind-example

# One of the new trends in Continuous Integration/Deployment is to:
# (see https://docs.gitlab.com/ee/ci/docker/using_docker_build.html)
#
# 1. Create an application image
# 2. Run tests against the created image
# 3. Push image to a remote registry
# 4. Deploy to a server from the pushed image

stages:
  - build
  - test
  - push
  - deploy

# see how to login at https://docs.gitlab.com/ee/ci/docker/using_docker_build.html#using-the-gitlab-container-registry
before_script:
  - docker login -u $CI_REGISTRY_USER -p $CI_JOB_TOKEN $CI_REGISTRY

build-image:
  stage: build
  # the tag 'dind' advices only GitLab runners using this tag to pick up that job
  tags: 
    - dind
  script:
    - docker build . --tag $REGISTRY_GROUP_PROJECT/gitlab-ci-dind-example:latest

test-image:
  stage: test
  tags:
    - dind
  script:
    - echo Insert fancy API test here!

push-image:
  stage: push
  tags:
    - dind
  script:
    - docker push $REGISTRY_GROUP_PROJECT/gitlab-ci-dind-example:latest

deploy-2-dev:
  stage: deploy
  tags:
    - dind
  script:
    - echo You should use Ansible here!
  environment:
    name: dev
    url: https://dev.jonashackt.io


```

#### layer caching for Docker-in-Docker

Possible optimisation: [Making docker-in-docker builds faster with Docker layer caching](https://docs.gitlab.com/ee/ci/docker/using_docker_build.html#making-docker-in-docker-builds-faster-with-docker-layer-caching): As the Docker Engine used with Docker-in-Docker will download the images every time the pipeline starts a build, it will slow down your builds. Here the `--cache-from` argument of the `docker run` command can help.


#### No modern full-Docker workflow possible!

Big problem: The "new trends in Continuous Integration/Deployment" aren't applieable with Docker-in-Docker - those are:

```
# One of the new trends in Continuous Integration/Deployment is to:
# (see https://docs.gitlab.com/ee/ci/docker/using_docker_build.html)
#
# 1. Create an application image
# 2. Run tests against the created image
# 3. Push image to a remote registry
# 4. Deploy to a server from the pushed image

stages:
  - build
  - test
  - push
  - deploy
``` 

Since we do our `docker build` inside the `build` stage - and the Pipeline tries to push the resulting Docker image in the `push` stage - but images aren't shared or kept up over stages - we can't implement this desired workflow. For more info, have a look at the overview ASCII art here: https://github.com/jonashackt/gitlab-ci-stack#configure-a-docker-in-docker-enabled-gitlab-runner-with-the-docker-executor

One possible workaround is to skip the 4 phases and do just a `build-push`, then `test` and then `deploy` - which brings the drawback of "releasing" un-tested images to our registry.


### Simple REST Spring Boot app

Start the app and go to [http://localhost:8080/v2/api-docs](http://localhost:8080/v2/api-docs) to see all endpoints as JSON.

UI-Documentation you will find under [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).