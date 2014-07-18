JFLAGS = -g -d bin
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	br/odb/gamelib/gameapp/ApplicationClient.java \
	br/odb/gamelib/gameapp/ConsoleApplication.java \
	br/odb/gamelib/gameapp/GameAssetManager.java \
	br/odb/gamelib/gameapp/GameAudioManager.java \
	br/odb/gamelib/gameapp/GameConfigurations.java \
	br/odb/gamelib/gameapp/GameEngineController.java \
	br/odb/gamelib/gameapp/GameResource.java \
	br/odb/gamelib/gameapp/GameRule.java \
	br/odb/gamelib/gameapp/GameSession.java \
	br/odb/gamelib/gameapp/GameUpdateDelegate.java \
	br/odb/gamelib/gameapp/MediaPlayerWrapper.java \
	br/odb/gamelib/gameapp/PositionalMediaPlayer.java \
	br/odb/gamelib/gameapp/SaveStateNotFound.java \
	br/odb/gamelib/gameapp/ScheduledEvent.java \
	br/odb/gamelib/gameapp/Timer.java \
	br/odb/gamelib/gameapp/Updatable.java \
	br/odb/gamelib/gameapp/UserCommandLineAction.java \
	br/odb/gamelib/gameworld/ActiveItem.java \
	br/odb/gamelib/gameworld/Actor.java \
	br/odb/gamelib/gameworld/Actor2D.java \
	br/odb/gamelib/gameworld/CharacterActor.java \
	br/odb/gamelib/gameworld/CharacterIsNotMovableException.java \
	br/odb/gamelib/gameworld/Direction.java \
	br/odb/gamelib/gameworld/Door.java \
	br/odb/gamelib/gameworld/exceptions/DoorActionException.java \
	br/odb/gamelib/gameworld/exceptions/InvalidCharacterHandlingException.java \
	br/odb/gamelib/gameworld/exceptions/InvalidLocationException.java \
	br/odb/gamelib/gameworld/exceptions/InvalidSlotException.java \
	br/odb/gamelib/gameworld/exceptions/InventoryManipulationException.java \
	br/odb/gamelib/gameworld/exceptions/ItemActionNotSupportedException.java \
	br/odb/gamelib/gameworld/exceptions/ItemNotFoundException.java \
	br/odb/gamelib/gameworld/GameAudioListener.java \
	br/odb/gamelib/gameworld/Item.java \
	br/odb/gamelib/gameworld/Kind.java \
	br/odb/gamelib/gameworld/Level.java \
	br/odb/gamelib/gameworld/Location.java \
	br/odb/gamelib/gameworld/MapCell.java \
	br/odb/gamelib/gameworld/Place.java \
	br/odb/gamelib/gameworld/PlaceXMLFactory.java \
	br/odb/gamelib/media/AbstractMediaPlayer.java \
	br/odb/gamelib/rendering/Animation.java \
	br/odb/gamelib/rendering/BorderedSquareRenderingNode.java \
	br/odb/gamelib/rendering/Constants.java \
	br/odb/gamelib/rendering/DisplayList.java \
	br/odb/gamelib/rendering/Frame.java \
	br/odb/gamelib/rendering/GameRenderer.java \
	br/odb/gamelib/rendering/LoopableMusic.java \
	br/odb/gamelib/rendering/PatternRenderingNode.java \
	br/odb/gamelib/rendering/PolygonRenderingNode.java \
	br/odb/gamelib/rendering/RasterImage.java \
	br/odb/gamelib/rendering/Rect.java \
	br/odb/gamelib/rendering/RenderingContext.java \
	br/odb/gamelib/rendering/RenderingNode.java \
	br/odb/gamelib/rendering/ResourceIdentificator.java \
	br/odb/gamelib/rendering/SolidSquareRenderingNode.java \
	br/odb/gamelib/rendering/Sound.java \
	br/odb/gamelib/rendering/Sprite.java \
	br/odb/gamelib/rendering/SVGDisplayListBuilder.java \
	br/odb/gamelib/rendering/SVGRenderingNode.java \
	br/odb/gamelib/rendering/Tile.java \
	br/odb/gamelib/rendering/VirtualPad.java \
	br/odb/gamelib/rendering/VirtualPadClient.java \
	br/odb/liboldfart/parser/FileFormatParser.java \
	br/odb/liboldfart/parser/GeometryDecoder.java \
	br/odb/liboldfart/parser/level/GEOLoader.java \
	br/odb/liboldfart/wavefront_obj/WavefrontOBJExporter.java \
	br/odb/liboldfart/wavefront_obj/WavefrontOBJLoader.java \
	br/odb/libscene/Actor.java \
	br/odb/libscene/ActorCommand.java \
	br/odb/libscene/ActorConstants.java \
	br/odb/libscene/Color.java \
	br/odb/libscene/Constants.java \
	br/odb/libscene/Door.java \
	br/odb/libscene/Group.java \
	br/odb/libscene/Hyperplane.java \
	br/odb/libscene/InvalidSectorQuery.java \
	br/odb/libscene/Material.java \
	br/odb/libscene/Mesh.java \
	br/odb/libscene/ObjMesh.java \
	br/odb/libscene/PartitionerListener.java \
	br/odb/libscene/PartitioningScheme.java \
	br/odb/libscene/PolygonFace.java \
	br/odb/libscene/Scene.java \
	br/odb/libscene/SceneObject3D.java \
	br/odb/libscene/Sector.java \
	br/odb/libscene/World.java \
	br/odb/libscene/WorldProcessor.java \
	br/odb/libscene/WorldUtils.java \
	br/odb/libstrip/AbstractSquare.java \
	br/odb/libstrip/AbstractSquareFactory.java \
	br/odb/libstrip/AbstractTriangle.java \
	br/odb/libstrip/AbstractTriangleFactory.java \
	br/odb/libstrip/GeneralPolygon.java \
	br/odb/libstrip/GeneralPolygonFactory.java \
	br/odb/libstrip/GeneralQuad.java \
	br/odb/libstrip/GeneralTriangle.java \
	br/odb/libstrip/IndexedSetFace.java \
	br/odb/libstrip/MeshFactory.java \
	br/odb/libstrip/PolygonalFace.java \
	br/odb/libstrip/VertexArrayManager.java \
	br/odb/libsvg/ColoredPolygon.java \
	br/odb/libsvg/ord.java \
	br/odb/libsvg/SVGGraphic.java \
	br/odb/libsvg/SVGParsingUtils.java \
	br/odb/libsvg/SVGUtils.java \
	br/odb/utils/FileServerDelegate.java \
	br/odb/utils/math/Matrix.java \
	br/odb/utils/math/Vec2.java \
	br/odb/utils/math/Vec3.java \
	br/odb/utils/Utils.java \
	br/odb/utils/VerbosityLevel.java \
	br/odb/worldprocessing/AddFirstMasterSector.java \
	br/odb/worldprocessing/DegenerateSectorCuller.java \
	br/odb/worldprocessing/GeometryCompiler.java \
	br/odb/worldprocessing/LogUtils.java \
	br/odb/worldprocessing/RemoveCoincidantSectors.java \
	br/odb/worldprocessing/RemoveFirstMasterSector.java \
	br/odb/worldprocessing/RemoveLeafSectors.java \
	br/odb/worldprocessing/SectorLinker.java \
	br/odb/worldprocessing/SectorSnapper.java \
	br/odb/worldprocessing/SequencialSectorIdSetter.java \
	br/odb/worldprocessing/StartSectorLocator.java \
	br/odb/worldprocessing/WorldLocalPartitioner.java

default: classes

classes: $(CLASSES:.java=.class)

jar: classes
	jar cf target\gamelib-java.jar bin\*

clean:
	$(RM) *.class
