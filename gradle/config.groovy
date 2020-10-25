withConfig(configuration) {
    // To skip processing @Grab annotations when running unit tests
    configuration.setDisabledGlobalASTTransformations(['groovy.grape.GrabAnnotationTransformation'] as Set)
}