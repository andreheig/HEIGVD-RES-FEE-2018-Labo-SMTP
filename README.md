# ANDRE HEIG SPAM PROJET

## Description
Ce projet permet de creer des spam à partir d'une liste d'adresse mail et de messages prédéfini. Tous les choix sont paramétrable via le fichier de configuration :

## MockMockServer (double test):
Permet de visulaliser les mail envoyé sans pour autant perturber un serveur SMTP, pratique pour les test  
Pour pouvoir utiliser MockMock:  
télécharger le .jar  
https://github.com/tweakers-dev/MockMock/blob/master/release/MockMock.jar?raw=true  
puis lancer une invite de commande en tapant:  
java -jar MockMock.jar  
possibilité de changer le noméro de port avec -p, et -h pour changer l'adresse d'écoute de l'interface Web comme ceci:  
java -jar MockMock.jar -p <numéro de port> -h <numéro de port>  
Il suffit de fermer le terminal qui a lancer MockMock afin de terminer le processus.  

## Configuration :
Après avoir forker / cloner le repo, il suffit de :  
Avoir JDK / JRE d'installer afin de pouvoir utiliser un IDE  
Avoir un IDE d'installer  
lancer votre IDE, et aller dans implémentation => src => config.properties  
Configurer les différents paramètres suivants:  
SMTPServer : désigne le serveur smtp avec lequel on souhaite communiquer  
port :  désigne le port sur lequel on doit écouter (par défaut 25)  
numberofgroup : permet de spécifier le nombre de groupe minimum (0 génération automatique)  
numberofvictim : permet de spécifier le nombre de victime minimum (0 génération automatique)  
mail : permet de spécifier le numéro de mail type désiré (à partir de 1)  
subject : sujet de(s) email(s)  
name : nom du logon echo  
Lancer le Main.java afin de lancer la créations des spams.  