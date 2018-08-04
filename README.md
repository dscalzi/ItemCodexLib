# ItemCodexLib
[<img src="https://ci.appveyor.com/api/projects/status/ijrv4lfx1h3r7e1f" height='20.74px'></img>](https://ci.appveyor.com/project/dscalzi/itemcodexlib) ![](https://img.shields.io/github/license/dscalzi/ItemCodexLib.svg)

ItemCodexLib is a lightweight library which enables your plugin to store aliases for items. It is the successor to the legacy items.csv format. Written in JSON, ItemCodex fully supports Mojang's new item identification system. Items which store NBT data, such as potions or tipped arrows, are also supported.

## Format

```JSON
{
  "version": "1.13",
  "revision": "1.0.0",
  "items": []
}
```

Item objects are added to the `items` array.

The `revision` field is used to update the JSON file. If the stored JSON file's version is less than the internal version, it will be updated. If for whatever reason you're using a modified version of this file and do not want it to be updated, change it's version to something large.

### Example Items

#### Charcoal (no NBT data)

```JSON
{
  "spigot": {
    "material": "CHARCOAL"
  },
  "legacy": {
    "id": 263,
    "data": 1
  },
  "aliases": [
    "charcoal",
    "ccoal"
  ]
}
```

#### Lingering Potion of Strength II (NBT data)

```JSON
{
  "spigot": {
    "material": "LINGERING_POTION",
    "potionData": {
      "type": "STRENGTH",
      "extended": false,
      "upgraded": true
    }
  },
  "legacy": {
    "id": 441,
    "data": 0
  },
  "aliases": [
    "lingerpotstrengthii",
    "strengthlingerpotii",
    "lingerpotstrii",
    "strlingerpotii"
  ]
}
```

## Library

ItemCodex comes with a utility library to enable seamless integration into your plugin. All you need to do is add it as a dependency with Maven/Gradle.

### Usage

Usage is simple. Simply create a new `ItemCodex` object and query it.

#### Note: Cache the `ItemCodex` object, as the entire codex is loaded each time you create a new instance.

```Java
public void itemCodexExample() {
    // Load the items file and setup the mappings.
    ItemCodex codex = new ItemCodex(this.plugin);
    
    // Get stone by its material name.
    codex.getItem("STONE").ifPresent((entry) -> {
        ItemStack stone = entry.getItemStack();
    });
    
    // Get stone by an alias.
    codex.getItem("rock").ifPresent((entry) -> {
        ItemStack stone = entry.getItemStack();
    });
    
    // Get stone by its legacy id.
    codex.getItem("1").ifPresent((entry) -> {
        ItemStack stone = entry.getItemStack();
    });
    
    // Get stone by its legacy id and data value.
    // Let's use a different Optional construct.
    Optional<ItemEntry> entryOptional = codex.getItem("1:0");
    if(entryOptional.isPresent()) {
        ItemStack stone = entryOptional.get().getItemStack();
    }
}
```

## Maven / Gradle

The library will be hosted on JCenter when everything is finalized. If you would like to contribute or test the library's progress in your plugin before then, you can build locally.

### Local Gradle Installation

* Clone the repository and run `gradle install`

In your plugin's build.gradle:
```gradle
repositories {
    mavenLocal()
}

dependencies {
    compile group: 'com.dscalzi', name: 'ItemCodexLib', version: 'VERSION'
}
```

### Local Maven Installation

* Clone the repo and run `mvn install`

In your plugin's pom.xml:
```xml
<dependency>
    <groupId>com.dscalzi</groupId>
    <artifactId>ItemCodexLib</artifactId>
    <version>VERSION</version>
</dependency>
```
