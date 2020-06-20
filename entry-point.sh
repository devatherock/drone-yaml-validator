ENABLE_DEBUG=false
if [ "$PLUGIN_DEBUG" = "true" ] || [ "$PARAMETER_DEBUG" = "true" ]; then
  ENABLE_DEBUG=true
fi

/scripts/yamlvalidator --debug $ENABLE_DEBUG