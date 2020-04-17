@Grab(group = 'org.yaml', module = 'snakeyaml', version = '1.25')
@Grab(group = 'net.sourceforge.argparse4j', module = 'argparse4j', version = '0.8.1')

import groovy.transform.Field
import java.util.logging.Logger
import java.util.logging.Level
import org.yaml.snakeyaml.Yaml
import net.sourceforge.argparse4j.inf.ArgumentParser
import net.sourceforge.argparse4j.ArgumentParsers

System.setProperty('java.util.logging.SimpleFormatter.format', '%5$s%n')
@Field static final Logger LOGGER = Logger.getLogger('YamlValidator.log')
@Field boolean debug

ArgumentParser parser = ArgumentParsers.newFor('YamlValidator').build()
        .defaultHelp(true)
parser.addArgument('-d', '--debug')
        .choices(true, false).setDefault(false)
        .type(Boolean)
        .help('Flag to turn on debug logging')

final String[] args = getProperty('args') as String[]
def options = parser.parseArgs(args)
debug = options.getBoolean('debug')

if(debug) {
    Logger root = Logger.getLogger('')
    root.setLevel(Level.FINE)
    root.getHandlers().each { it.setLevel(Level.FINE) }
}

@Field Yaml yaml = new Yaml()
validateYamlFiles(new File((String) System.properties['user.dir']))

/**
 * Validates all yaml files in the provided directory recursively
 *
 * @param directory
 * @return
 */
def validateYamlFiles(File directory) {
    LOGGER.fine("Validating files in '${directory}'")

    String fileName
    directory.eachFile { file ->
        if (file.isDirectory()) {
            // Recursively evaluate yaml files in each folder
            validateYamlFiles(file)
        } else if (file.isFile()) {
            fileName = file.absolutePath
            if (fileName.endsWith('.yaml') || fileName.endsWith('.yml')) {
                LOGGER.fine("Validating '$fileName'.")
                int index = 1

                file.withInputStream { yamlFileInputStream ->
                    index = 1

                    try {
                        yaml.loadAll(yamlFileInputStream).each { document ->
                            LOGGER.fine("Document $index of '$fileName' is valid")
                            index++
                        }
                    }
                    catch (Exception e) {
                        LOGGER.log(Level.SEVERE, "'${fileName}' is invalid", e)
                        System.exit(1)
                    }
                }
                LOGGER.info("'$fileName' is valid")
            }
        }
    }
}