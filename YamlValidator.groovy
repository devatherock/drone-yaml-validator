
@Grapes([
    @Grab(group = 'org.yaml', module = 'snakeyaml', version = '1.20')
])
import groovy.transform.Field
import java.util.logging.Logger
import java.util.logging.Level
import org.yaml.snakeyaml.Yaml
import java.util.function.Supplier

System.setProperty('java.util.logging.SimpleFormatter.format',
        '%1$tY-%1$tm-%1$tdT%1$tH:%1$tM:%1$tS.%1$tL%1$tz %4$s %5$s%6$s%n')
@Field static final Logger LOGGER = Logger.getLogger('YamlValidator.log')
@Field boolean debug

def cli = new CliBuilder(usage: 'groovy YamlValidator.groovy [options]')
cli._(longOpt: 'debug', args: 1, argName: 'debug', 'Flag to turn on debug logging')
cli._(longOpt: 'test', args: 0, argName: 'test', 'Flag to build grape cache and exit')

def options = cli.parse(args)
if(options.test) {
    LOGGER.info({"Grape cache built".toString()})
    System.exit(0)
}


debug = Boolean.parseBoolean(options.debug)

@Field Yaml yaml = new Yaml()
validateYamlFiles(new File(System.properties['user.dir']))

/**
 * Validates all yaml files in the provided directory recursively
 *
 * @param directory
 * @return
 */
def validateYamlFiles(File directory) {
    debugLog({ "Starting validation of YAML files in directory '${directory}'.".toString() })

    String fileName
    directory.eachFile { file ->
        if (file.isDirectory()) {
            // Recursively evaluate yaml files in each folder
            validateYamlFiles(file)
        } else if (file.isFile()) {
            fileName = file.absolutePath
            if (fileName.endsWith('.yaml') || fileName.endsWith('.yml')) {
                debugLog({"Starting validation of YAML file '$fileName'.".toString()})
                int index = 1

                file.withInputStream { yamlFileInputStream ->
                    index = 1

                    try {
                        yaml.loadAll(yamlFileInputStream).each { document ->
                            debugLog({ "Document $index of '$fileName' is valid".toString() })
                            index++
                        }
                    }
                    catch (Exception e) {
                        LOGGER.log(Level.SEVERE, "Validation of YAML file '${fileName}' failed.", e)
                        System.exit(1)
                    }
                }
                LOGGER.info("Validation of YAML file '$fileName' successful.")
            }
        }
    }
}

/**
 * Log the provided message if debug is enabled
 *
 * @param message
 */
void debugLog(Supplier message) {
    if (debug) {
        LOGGER.info(message)
    }
}