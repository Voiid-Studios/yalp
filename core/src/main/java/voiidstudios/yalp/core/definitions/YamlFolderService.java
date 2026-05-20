package voiidstudios.yalp.core.definitions;

public interface YamlFolderService {
    YamlDefinitionRegistry load(Object owner, String folderName, String rootSection);
}
