## MatrixScape
#### MatrixScape is a simple and straightforward interpreted command language for calculating linear algebra problems

There are two versions of MatrixScape. One is written in [Java](https://github.com/JCurran0499/MatrixScape/tree/master/Java/MatrixScape) and the other in Python (coming soon).

### Download and Installation

The download tools provided to you in this repository are based on the Linux OS distribution popular with AWS EC2 instances. For other servers and virtual machines, the commands may be slightly different. <br/> <br/>
To check that your OS matches this, run `yum --version` and verify that your OS recognizes the YUM package installer. <br/>
If not, then you must install Git and Java in a way that matches your OS. Once this is done, the project will run on your OS perfectly fine. 
<br/>
<br/>
#### Download Git
```
sudo yum install git
git --version
```

#### Download Project Repo
```
git clone https://github.com/JCurran0499/MatrixScape.git
```

#### Give .sh Permissions and Install Java
```
cd MatrixScape
chmod u+x start.sh
chmod u+x install_java.sh
./install_java.sh
java --version
```

#### Run the Program
```
./start.sh
```
