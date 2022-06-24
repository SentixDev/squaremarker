import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

abstract class SquareMarkerPlatformExtension @Inject constructor(objects: ObjectFactory) {
    val productionJar = objects.fileProperty()
}
