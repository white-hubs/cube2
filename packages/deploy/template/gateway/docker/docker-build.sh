#!/usr/bin/env bash
source $(dirname "$0")/bash-tools.sh
PARAM_TAG=unknown
PARAM_REGISTRY=unknown
PARAM_IMAGE=unknown
PARAM_ARCH=unknown

# Get parameters from the command line. tools from bash-tools.sh
allowed_params="help h|tag t|registry r|image i|arch a"
get_shell_params $*
# Get parameters ended

IMAGE=${PARAM_IMAGE}:${PARAM_TAG}
REGISTRY_IMAGE=${PARAM_REGISTRY}/${IMAGE}

docker buildx use builder
docker buildx build -f Dockerfile --platform ${PARAM_ARCH} -t ${REGISTRY_IMAGE} --push .
