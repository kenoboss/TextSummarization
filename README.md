# Projektseminar WS 17/18
## Textsummarization

Das Ziel des Projektes bestand darin, automatische Textzusammenfassungen unter der Verwendung eines neuronalen Netzes zu erzeugen.
Für die Erzeugung, das Training und die Anwendung des neuronalen Netztes wurde DL4J (https://deeplearning4j.org/) verwendet, eine Library für
Anwendungen des Deeplearning in Java. Darüber hinaus wurden an externen Quellen die Stanford NLP Library (https://nlp.stanford.edu/), sowie ein über die Machine Learning
Platform Kaggle (www.kaggle.com) bezogenes Korpus verwendet. Da die Zusammenfassungen im verwendeten Korpus manuell erstellt sind, wurde
ein Distanzmaß zur späteren Evaluierung der erstellten Zusammenfassungen verwendet, um die Ergebnisse vergleichbar zu machen.

### 1) Ansatz (Khosrow Kaikhah. Text Summarization Using Neural Networks):
Zur Abstraktion und Evaluation der einzelnen Sätze eines Textes wurde auf Grundlage des oben genannten Artikels ein Set aus Features ausgewählt,
die den jeweiligen Satz repräsentieren.
Folgende Merkmale wurden dazu herangezogen:
  - Erster Satz im Text
  - Satzposition im Text
  - Satzlänge
  - Anzahl thematischer Wörter im Satz*
  - Anzahl an Wörtern im Satz, welche auch im Titel vorkommen
 
*Als thematische Wörter wurden die 10 häufigsten Wörter des Textes definiert.

#### Relevante Klassen: 
- Preprocessing.java (Featureextraktion aus Sätzen)
- FeatureVector.java (Featureextraktion und Erzeugung des Objekts)

### 2) Vorverarbeitung:
Das Korpus besteht aus einer .csv Datei, welche den Author, das Erscheinungsdatum, den Titel, die Quelle, die Zusammenfassung und den Text
beinhaltet. Auf Basis der .csv Datei wird  ein Corpus Objekt angelegt, welches Entries beinhaltet, die die Zeilen der csv. Datei repräsentieren, dabei
aber zusätzlich mit Informationen angereichert sind. An dieser Stelle kommt die Stanford NLP Library zum Einsatz, die zusätzlich eine tokenisierte
und lemmatisierte Repräsentation der Überschrift, des Textes und der Zusammenfassung enthält. Darüber hinaus werden die Inhaltswörter des 
Textes und der Überschrift abgespeichert. Die einzelnen Sätze werde mit der Zusammenfassung verglichen und aufgrund der euklidischen Distanz
zwischen Satz und Zusammenfassung wird ein Label vergeben, ob der jeweilige Satz in eine Zusammenfassung gehört, oder nicht. Zusätzlich
werden die in 1) beschriebenen Feature Vektoren für die jeweiligen Sätze erstellt und dem Korpus hinzugefügt.

#### Relevante Klassen:
- Corpus.java (Zur Erstellung des Korpus)
- Entry.java (Repräsentiert einen Eintrag im Korpus)
- LabelSentences.java (Vergibt auf Grundlage der euklidischen Distanzen Labels  (0|1))
- StanfordNLP.java (stellt Funktionen zur Tokenisierung und Lemmatisierung bereit)
- Helper.java (stellt Funktionen zum Einlesen von Dateien bereit)
- EuclideanDistance.java (berechnet die euklidische Distanz zwischen Sätzen)
- WordFrequencies.java (Bestimmt Wortfrequenzen)

### 3) Neuronales Netz:
Mit den extrahierten Featurevektoren und den Satzlabels wird nun ein Neuronales Netz trainiert (Input-, Hidden- und Outputlayer).
Die Anzahl der Neuronen in den Schichten beträgt 5 (=Anzahl Features), 20 und 2(=Anzahl Labels). Das Korpus wurde hierfür in ein Trainings-
und ein Testset aufgeteilt, die jeweils gelabelt sind. Auf Grundlage der Sets werden im Trainings- und Evaluationsprozess die Gewichte eingestellt,
die für die spätere Klassifikation verwendet werden. Das trainierte Netz wird gespeichert und kann nun verwendet werden. Neue Texte, die 
zusammengefasst werden sollen, werden dafür zunächst wieder wie in 2) beschrieben verarbeitet und mit Informationen angereichert. Der Output
des neuronalen Netzes bestimmt schließlich, ob der aktuelle Satz wichtig genug für die Zusammenfassung ist.

#### Relevante Klassen:
- NeuralNetwork.java (Erzeugung, Training und Speichern des Netzes)
- Classifier.java (Aufruf der Vorverarbeitungsroutine des Textes, Laden des Netzes und Klassifikation)

### 4) Graphische Nutzeroberfläche:
Zur leichteren Bedienung wurde eine GUI entwickelt, die es ermöglicht, einen Text mit Überschrift einzugeben, diesen zusammenfassen zu lassen
und die Zusammenfassung anzeigen zu lassen.

#### Relevante Klassen:
- GUIForSum.java
