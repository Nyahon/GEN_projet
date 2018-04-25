# Explication UseCase

## Acteur : Administrateur

### ajouter/Creer/Modifier

- L'administrateur a le pouvoir d'ajouter, créer, modifier des éléments de la base de donnée tel que des matières définissant le type de question existantes, des questions, des professeurs, des assistants.

### Modifier un joueur

- l'administrateur a la possibilité d'intérvenir sur le compte utilisateur d'un joueur et d'en modifier des caractéristiques.

### attribuer

- l'administrateur a la possibilité d'attribuer les questions aux matières correspondantes et vice-versa. Il peut aussi attribuer des matières a un professeur ou l'inverse.

## Acteur : joueur

### jouer

- Le joueur peut lancer l'application et choisir soit de jouer contre un serveur, soit de jouer contre un autre joueur.

- Pour cela le joueur devra avoir créé un compte via l'interface de création de compte.

### Créer un compte

- Cela inclus de passer par l'interface de création de compte afin de se connecter ou de s'ajouter en tant qu'utilisateur de l'application et donc de se créer un compte.

### joueur contre joueur

- Si le joueur souhaite jouer contre un autre joueur, il devra attendre la connexion de son adversaire.

- Le mode de jeu "joueur contre joueur" inclus le fait de poser des questions à l'adversaires puis de répondre aux siennes. A travers ce jeu de questions réponses plusieurs événement peuvent se passer tel que perdre et donc perdre des points de classements.

### gagne objet si gagne

- Si le joueur gagne le "combat" contre son adversaire, celui-ci peut récupérer certains objet pouvant l'aider a avancer dans le jeu.

### gagne points de classement si gagne

- Si le joueur gagne le "combat" contre son adversaire, il va alors gagner des points de classement.

### perd points de classement si perd

- Si le joueur perd le "combat" contre son adversaire, il va alors perdre des points de classement.

### pose une question

- Durant un "combat" contre un adversaire, chaque joueur pose a tour de rôle une question qu'il puise dans la liste de question a disposition.

### Sélectionner une question parmi celles gagnées

- Lorsque un joueur pose une question, il en choisit une parmis celle gagnée dans le mode histoire.

### joueur contre serveur

- le mode de jeu "joueur contre serveur" inclus le fait d'avancer dans l'aventure qui inclus de combattre des professeurs/assistants.

- un combat contre un professeur/assistant inclus de répondre aux questions de "l'ennemi" si on répond juste on passe à la question suivante.

- Si un joueur "tue" un ennemi, il gagne un niveau et passe à l'ennemi suivant.

- Si un joueur tombe à zéro points de vie, il perd et meurt. Il doit donc recommencer le combat.

- Si un joueur réussit à combattre un professeur/assistant il gagne toutes les questions répondues justes et les ajoutes à son "pool" de questions. Ces questions lui seront utiles en mode joueur contre joueur.

### combattre une professeur ou un assistant

- Durant le mode histoire "contre serveur" le joueur va devoir d'attaquer à des assistants puis les professeur. Celui-ci devra les battre afin de passer a l'étape suivante.

### prochain niveau si gagne

- Si un joueur combat un assistant ou un professeur il peut alors passer au niveau suivant lui proposant alors un nouvel adversaire.

### gagne expérience si gagne

- Si un joueur gagne dans un combat en mode histoire, il va alors gagner en retour des points d'expérience.

### récupère question si gagne

- Si un joueur gagne dans un combat en mode histoire, Les questions répondues juste sont alors ajoutées a son pool de questions qu'il peut poser dans le mode joueur contre joueur.

### perdre et mourir

Lorsque que le joueur tombe a zéro point de vie, alors il meurt et devra recommencer le niveau.

### Répondre a une question

- Répondre au question inclus que si on répond faux, on perd des points de vie. Si on tombe à zéro on perd le combat.

- Si un joueur est en difficulté pour répondre à une question il peut choisir d'utiliser un Objet qui va l'aider en modifiant de comportement du jeux et en lui facilitant la tâche (dépend du jocker utilisé).

### perdre des points de vie

- Lorsque le joueur répond faux a une question il perd alors des points de vie.

### utiliser un objet

- Durant les combats, un joueur peut utiliser un objet se trouvant a ça disposition dans son inventaire. Cet objet peut être de type différent l'aidant alors a répondre a la question ou a re-gagner des points de vie.
