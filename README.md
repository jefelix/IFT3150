IFT3150
=======

Visualisation de résultats d'analyse dans Eclipse.

Les résultats obtenus par analyse de programmes doivent très souvent être communiqués aux développeurs. Une possibilité consiste à afficher ces résultats directement dans leur environnement de développement afin de faciliter la compréhension du code.

Le projet proposé vise à développer une extension à l'environnement de développement Eclipse afin de présenter les résultats d'une analyse directement dans l'éditeur de code. Cette extension devra permettre d'afficher plusieurs types d'information à des points donnés dans le code source. Les types d'information qui devront être supportés sont:

* Information textuelle: affichée simplement à l'aide d'un "popup", ou un mécanisme similaire.
* Information numérique: pourrait être affichée comme une couleur calculée à partir d'une interpolation entre deux couleurs pour les valeurs extrêmes, en plus d'être affichée comme une information textuelle.
* Liens entre éléments: l'extension devra permettre de relier deux éléments dans le code source, soit visuellement, soit par une liste d'hyperliens (un élément peut être relié à plus d'un autre élément).

Par souci d'intéropérabilité avec des systèmes existants, le format de données à utiliser sera spécifié. Des données générées préalablement à partir d'outils et de programmes existants seront utilisées afin d'évaluer le système.
