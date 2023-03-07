import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

abstract class SquareMarkerPlatformExtension {
    abstract val productionJar: RegularFileProperty

    abstract val modInfoFilePath: Property<String>
}
