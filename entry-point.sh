#!/bin/sh

ENABLE_DEBUG=false
if [ "$PLUGIN_DEBUG" = "true" ] || [ "$PARAMETER_DEBUG" = "true" ]; then
  ENABLE_DEBUG=true
fi

CONTINUE_ON_ERROR=true
if [ "$PLUGIN_CONTINUE_ON_ERROR" = "false" ] || [ "$PARAMETER_CONTINUE_ON_ERROR" = "false" ]; then
  CONTINUE_ON_ERROR=false
fi

ALLOW_DUPLICATE_KEYS=false
if [ "$PLUGIN_ALLOW_DUPLICATE_KEYS" = "true" ] || [ "$PARAMETER_ALLOW_DUPLICATE_KEYS" = "true" ]; then
  ALLOW_DUPLICATE_KEYS=true
fi

if [ ! -z "$PLUGIN_SEARCH_PATH" ]; then
  SEARCH_PATH=$PLUGIN_SEARCH_PATH
elif [ ! -z "$PARAMETER_SEARCH_PATH" ]; then
  SEARCH_PATH=$PARAMETER_SEARCH_PATH
fi

ALL_OPTS="--debug $ENABLE_DEBUG -c $CONTINUE_ON_ERROR -ad $ALLOW_DUPLICATE_KEYS"
if [ ! -z "$SEARCH_PATH" ]; then
   ALL_OPTS="$ALL_OPTS -p $SEARCH_PATH"
fi

/scripts/yamlvalidator $ALL_OPTS