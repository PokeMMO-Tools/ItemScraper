
rootProject.name = "ItemScraper"

include(":core")
include(":pokemmo")

project(":core").projectDir = file("core")
project(":pokemmo").projectDir = file("pokemmo")
