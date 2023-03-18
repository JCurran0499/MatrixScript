## MatrixScript Backend
#### MatrixScript is a simple and straightforward interpreted command language for calculating linear algebra problems

There are two versions of MatrixScript. One is written in Java (here) and serves as an API. The other is in Python (coming soon) and will serve as a Lambda function behind an AWS API Gateway.

### Frontend and Backend Servers

This project is designed to be run on two different servers, a frontend and backend. This repository represents the backend, and below are instructions for setting up and running the backend server. The frontend can be found [here](https://github.com/JCurran0499/MatrixScript-Frontend)<br/>
In many cases, especially your local device, it is acceptable to run both on the same host. However, it is best practice to set up independent public servers (ex: virtual machines) for this purpose. <br/> <br/>
The backend will be accessed exclusively by the frontend on port 4567. Configure your Security Group as such.

### Download and Installation

The download tools provided to you in this repository are based on the Red Hat Linux OS distributions popular with basic AWS EC2 instances, including AWS Linux, CentOS, and Fedora. For other servers and virtual machines, the commands may be slightly different. <br/> <br/>
To check that your OS matches this distribution, run `yum --version` and verify that your OS recognizes the YUM package installer. <br/>
If not, then you must install Git and Java in a way that matches your OS. Once this is done, the project will run on your OS perfectly fine. 

<br/>
<br/>
### Backend Setup
#### Download Git
```
sudo yum install git
git --version
```

#### Download Project Repo
```
git clone https://github.com/JCurran0499/MatrixScript.git
```

#### Give .sh Permissions and Install Java
```
cd MatrixScript
chmod u+x backend/start.sh
chmod u+x install_backend.sh
./install_backend.sh
```

#### Check Version
```
java --version
```

#### Allow Frontend CORS Permissions
Use `cd backend` to enter the backend directory. From here, edit the **.env** file, and fill in your front server's IP address or domain in the `FRONTEND` value. This way, your web app backend will be able send CORS permissions to your frontend.
<br/>
<br/>

#### Run the Program
```
cd backend
./start.sh
```

From here, the MatrixScript API can be accessed on port 4567, at `<server_domain_or_ip>:4567`.
