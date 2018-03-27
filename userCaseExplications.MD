# Explication UseCase

## Acteur : Administrateur

### Gérer les questions

- L'administrateur peut ajouter, modifier, supprimer des questions du "pool" de question via l'application.

## Acteur externe : Pool de questions

- le "pool" de questions sera sous la forme d'une base de donnée offrant une API d'utilisation afin d'effectuer les requêtes nécéssaires.

## Acteur : joueur

### jouer

- Le joueur peut lancer l'application et choisir soit de jouer contre un serveur, soit de jouer contre un autre joueur.

- Pour cela le joueur devra avoir créé un compte via l'interface de création de compte.

### joueur contre joueur

- Si le joueur souhaite jouer contre un autre joueur, il devra attendre la connexion de son adversaire.

- Le mode de jeu "joueur contre joueur" inclus le fait de poser des questions à l'adversaires puis de répondre aux siennes.

### joueur contre serveur

- le mode de jeu "joueur contre serveur" inclus le fait d'avancer dans l'aventure qui inclus de combattre des professeurs/assistants.

- un combat contre un professeur/assistant inclus de répondre aux questions de "l'ennemi" si on répond juste on passe à la question suivante.

- Si un joueur "tue" un ennemi, il gagne un niveau et passe à l'ennemi suivant.

- Si un joueur tombe à zéro points de vie, il perd et meurt. Il doit donc recommencer le combat.

- Si un joueur réussit à combattre un professeur/assistant il gagne toutes les questions répondues justes et les ajoutes à son "pool" de questions. Ces questions lui seront utiles en mode joueur contre joueur.

### Répondre a une question

- Répondre au question inclus que si on répond faux, on perd des points de vie. Si on tombe à zéro on perd le combat.

- Si un joueur est en difficulté pour répondre à une question il peut choisir d'utiliser un Jocker qui va l'aider en modifiant de comportement du jeux et en lui facilitant la tâche (dépend du jocker utilisé).

### Poser une question.

- poser une question dans le mode joueur contre joueur inclus de puiser des questions dans le "pool" de questions gagnées dans le mode histoire. 
