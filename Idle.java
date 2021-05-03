//enumération des différentes valeurs de ralenti (idle) = quand l'ascenseur n'a plus rien à faire
public enum Idle {
    STAY, //reste à son étage actuel
    INF, //descend d'un étage s'il n'est pas à l'étage 0
    SUP, //monte d'un étage s'il n'est pas au dernier étage
    MIDDLE //se déplace à l'étage au milieu de l'immeuble
}
