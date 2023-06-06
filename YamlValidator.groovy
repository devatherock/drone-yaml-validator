@Grab(group = 'org.yaml', module = 'snakeyaml', version = '1.27')
@Grab(group = 'net.sourceforge.argparse4j', module = 'argparse4j', version = '0.9.0')

import groovy.transform.Field
import java.util.logging.Logger
import java.util.logging.Level
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.error.YAMLException
import org.yaml.snakeyaml.LoaderOptions
import net.sourceforge.argparse4j.inf.ArgumentParser
import net.sourceforge.argparse4j.inf.ArgumentParserException
import net.sourceforge.argparse4j.ArgumentParsers

System.setProperty('java.util.logging.SimpleFormatter.format', '%5$s%6$s%n')
@Field static final Logger LOGGER = Logger.getLogger('YamlValidator.log')
@Field boolean debug
@Field boolean shouldContinue
@Field boolean allowDuplicateKeys

ArgumentParser parser = ArgumentParsers.newFor('YamlValidator').build()
        .defaultHelp(true)
parser.addArgument('-d', '--debug')
        .choices(true, false).setDefault(false)
        .type(Boolean)
        .help('Flag to turn on debug logging')
parser.addArgument('-c', '--continue')
        .choices(true, false).setDefault(true)
        .type(Boolean)
        .help('Flag to indicate if processing should be continued on error')
parser.addArgument('-p', '--path')
        .setDefault(System.getProperty('user.dir'))
        .type(String)
        .help('Path in which to look for yaml files')
parser.addArgument('-ad', '--allow-duplicate-keys')
        .choices(true, false).setDefault(false)
        .type(Boolean)
        .help('Flag to indicate if YAML files with duplicate keys should be considered valid')

final String[] ARGS = getProperty('args') as String[]
def options
try {
    options = parser.parseArgs(ARGS)
} catch (ArgumentParserException e) {
    parser.handleError(e)
    exitWithError()
}
options = parser.parseArgs(ARGS)
debug = options.getBoolean('debug')
shouldContinue = options.getBoolean('continue')
allowDuplicateKeys = options.getBoolean('allow_duplicate_keys')

if (debug) {
    Logger root = Logger.getLogger('')
    root.setLevel(Level.FINE)
    for (def handler : root.handlers) {
        handler.setLevel(Level.FINE)
    }
}

boolean isError = validateYamlFiles(new File(options.getString('path')))
if (isError) {
    exitWithError()
}

/**
 * Validates all yaml files in the provided directory recursively
 *
 * @param directory
 * @return
 */
boolean validateYamlFiles(File directory) {
    LOGGER.fine("Validating files in '${directory}'")

    LoaderOptions loaderOptions = new LoaderOptions(
            allowDuplicateKeys: allowDuplicateKeys
    )
    Yaml yaml = new Yaml(loaderOptions)
    String fileName
    boolean isError = false

    for (def file : directory.listFiles()) {
        LOGGER.fine("File or directory: ${file.absolutePath}")
        // Recursively evaluate yaml files in each folder
        if (file.directory) {
            // To retain overall error status. isError has to be on the right hand side
            isError = validateYamlFiles(file) || isError
        } else if (file.file) {
            fileName = file.absolutePath
            LOGGER.fine("File: ${file.absolutePath}")
            if (fileName.endsWith('.yaml') || fileName.endsWith('.yml')) {
                LOGGER.fine("Validating '$fileName'.")
                int index = 1

                file.withInputStream { yamlFileInputStream ->
                    try {
                        for (def document : yaml.loadAll(yamlFileInputStream)) {
                            LOGGER.fine("Document $index of '$fileName' is valid")
                            index++
                        }
                        LOGGER.info("'$fileName' is valid")
                    }
                    catch (YAMLException e) {
                        LOGGER.log(Level.SEVERE, "'${fileName}' is invalid. Error: ${e.message}")

                        if (shouldContinue) {
                            isError = true
                        } else {
                            exitWithError()
                        }
                    }
                }
            }
        }
    }

    return isError
}

/**
 * Exits the script because invalid yaml files have been encountered
 */
void exitWithError() {
    System.exit(1)
}