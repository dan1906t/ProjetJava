#Infinity Loop

Ce projet est l'implémentation du projet de java avancé InfinityLoop.

###Compilation

- Nécessite Java 11 et maven
- À la racine du projet, exécuter `mvn install`

###Exécution

- À la racine du projet, exécuter `java -jar target/InfinityLoop-0.0.1-SNAPSHOT.jar |options]` avec les `options` suivantes :
- `--gui` ou `-i` pour afficher l'interface graphique interactive
- `--generate [width]x[height] --output [file]` ou `-g [width]x[height] -o [file]` pour générer une grille de largeur `width` et hauteur `height` et la sauvegarder dans le fichier `file`
- `--check [file]` ou `-c [file]` pour vérifier si la grille dans le fichier `file` est résolue
- `--solve [inputfile] --output [outpufile]` ou `-s [inputfile] -o [outpufile]` pour résoudre la grille dans le fichier `inputfile` et sauvegarder le résultat dans le fichier `ouputfile`
- `--help` ou `-h` pour afficher la liste des options
- `--verbose` ou `-v` pour afficher plus d'informations en console
- `--display` ou `-d` pour afficher la grille dans une interface graphique
- `--nbcc [nbcc]` ou `-x [nbcc]` pour spécifier un nombre de composants connectés souhaité à la génération (entier)
- `--connectivity [connectivity]` ou `-y [connectivity]` pour spécifier une connectivité souhaité à la génération (réel entre 0 et 1)
- `--strategy` ou `-u` pour spécifier une stratégie à la résolution
- `--performances` ou `-p` pour afficher le temps mis à résoudre