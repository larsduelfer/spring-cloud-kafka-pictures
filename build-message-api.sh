#!/bin/bash
shopt -s expand_aliases
source "$(dirname ${BASH_SOURCE[0]})/build-utils.sh"

progress "BUILD COMMON-MESSAGE-API"
build_publish "message-api/common-message-api"

progress "BUILD USER-MESSAGE-API"
build_publish "message-api/user-message-api"

progress "BUILD IMAGE-MESSAGE-API"
build_publish "message-api/image-message-api"

progress "BUILD COMMENT-MESSAGE-API"
build_publish "message-api/comment-message-api"
