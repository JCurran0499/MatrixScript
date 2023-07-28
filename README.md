## MatrixScript Backend
#### MatrixScript is a simple and straightforward interpreted command language for calculating linear algebra problems

This version of MatrixScript is written in Java (here) and serves as an API, fronted by a [Node.js React web layer](https://github.com/JCurran0499/MatrixScript-Frontend) found in another repository.

### Frontend and Backend Servers

This project is designed to be run on two different servers, a frontend and backend. This repository represents the **backend**, and below are instructions for setting up and running the backend server. The frontend can be found [here](https://github.com/JCurran0499/MatrixScript-Frontend).<br/>

### Download and Installation

The download tools provided to you in this repository are based on the Red Hat Linux OS distributions popular with basic AWS EC2 instances, including AWS Linux, CentOS, and Fedora. For other servers and virtual machines, the commands may be slightly different. <br/> <br/>
To check that your OS matches this distribution, run `yum --version` and verify that your OS recognizes the YUM package installer. <br/>
If not, then you must install Git and Java in a way that matches your OS. Once this is done, the project will run on your OS perfectly fine. 
<br/>

**Note**: this project runs on ```OpenJDK 17```, different version may be incompatible.
<br/>
<br/>

### Backend Setup
#### Requirements
- Java: ```OpenJDK 17```
- Maven: ```Maven 3.9```
<br/>
<br/>

#### Download Project Repo
```
git clone https://github.com/JCurran0499/MatrixScript-Backend.git
cd MatrixScript-Backend
```
<br/>

#### Frontend CORS Permissions
If desired, edit the **.env** file, and fill in your frontend server's IP address or domain in the `FRONTEND` value. This way, your web app backend will be able send CORS permissions to your frontend. By default, this value is set to * and therefore allows all domains to access the backend. This step is optional, and is only used for security purposes to restrict backend access to your frontend domain alone.
<br/>
<br/>

#### Build the Program
This is an optional manual step, as it can be covered by the run script below.
```
mvn clean package
```
<br/>

#### Run the Program
```
./run.sh
```

From here, the MatrixScript API can be accessed on port 4567, at `<server_domain_or_ip>:4567`.
