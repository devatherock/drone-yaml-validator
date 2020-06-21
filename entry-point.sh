ENABLE_DEBUG=false
if [ "$PLUGIN_DEBUG" = "true" ] || [ "$PARAMETER_DEBUG" = "true" ]; then
  ENABLE_DEBUG=true
fi

SEARCH_PATH=$PLUGIN_SEARCH_PATH
if [ "$VELA" = "true" ]; then
  if [ ! -z "$PARAMETER_SEARCH_PATH" ]; then
    SEARCH_PATH=$PARAMETER_SEARCH_PATH
  fi
fi

ALL_OPTS="--debug $ENABLE_DEBUG"
if [ ! -z "$SEARCH_PATH" ]; then
   ALL_OPTS="$ALL_OPTS -p $SEARCH_PATH"
fi

/scripts/yamlvalidator $ALL_OPTS