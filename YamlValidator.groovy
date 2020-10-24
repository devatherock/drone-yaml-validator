@Grab(group = 'org.yaml', module = 'snakeyaml', version = '1.25')
@Grab(group = 'net.sourceforge.argparse4j', module = 'argparse4j', version = '0.8.1')

import groovy.transform.Field
import java.util.logging.Logger
import java.util.logging.Level
import org.yaml.snakeyaml.Yaml
import net.sourceforge.argparse4j.inf.ArgumentParser
import net.sourceforge.argparse4j.inf.ArgumentParserException
import net.sourceforge.argparse4j.ArgumentParsers

System.setProperty('java.util.logging.SimpleFormatter.format', '%5$s%6$s%n')
@Field Logger logger = Logger.getLogger('YamlValidator.log')
@Field boolean debug
@Field boolean shouldContinue

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

final String[] args = getProperty('args') as String[]
def options
try {
    options = parser.parseArgs(args)
} catch (ArgumentParserException e) {
    parser.handleError(e)
    System.exit(1)
}
options = parser.parseArgs(args)
debug = options.getBoolean('debug')
shouldContinue = options.getBoolean('continue')

if (debug) {
    Logger root = Logger.getLogger('')
    root.setLevel(Level.FINE)
    root.getHandlers().each { it.setLevel(Level.FINE) }
}

@Field Yaml yaml = new Yaml()
boolean isError = validateYamlFiles(new File(options.getString('path')))

if (isError) {
    System.exit(1)
}

/**
 * Validates all yaml files in the provided directory recursively
 *
 * @param directory
 * @return
 */
boolean validateYamlFiles(File directory) {
    logger.fine("Validating files in '${directory}'")

    String fileName
    boolean isError = false

    directory.eachFile { file ->
        logger.fine("File or directory: ${file.absolutePath}")
        // Recursively evaluate yaml files in each folder
        if (file.isDirectory()) {
            // To retain overall error status. isError has to be on the right hand side
            isError = validateYamlFiles(file) || isError
        } else if (file.isFile()) {
            fileName = file.absolutePath
            logger.fine("File: ${file.absolutePath}")
            if (fileName.endsWith('.yaml') || fileName.endsWith('.yml')) {
                logger.fine("Validating '$fileName'.")
                int index = 1

                file.withInputStream { yamlFileInputStream ->
                    index = 1

                    try {
                        yaml.loadAll(yamlFileInputStream).each { document ->
                            logger.fine("Document $index of '$fileName' is valid")
                            index++
                        }
                        logger.info("'$fileName' is valid")
                    }
                    catch (Exception e) {
                        logger.log(Level.SEVERE, "'${fileName}' is invalid. Error: ${e.getMessage()}")

                        if (shouldContinue) {
                            isError = true
                        } else {
                            System.exit(1)
                        }
                    }
                }
            }
        }
    }

    return isError
}