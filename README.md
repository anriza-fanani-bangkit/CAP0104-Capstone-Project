# Angkoot Project by CAP0104 Capstone Project team Bangkit Academy 2021

## Overview
Angkoot is an app that has a purpose to digitalize Angkot Transportation in Surabaya, Indonesia. The services that Angkoot provides now are fare prediction for the order, trip distance estimation, and user registration.

## Credits
For the fare prediction we are inspired from Dimitre Oliveira:
https://github.com/dimitreOliveira/NewYorkCityTaxiFare

And the fare prediction dataset that we used is from Kaggle. [[dataset]](https://www.kaggle.com/c/new-york-city-taxi-fare-prediction)

The deployment process with Flask to Google Compute Engine has been done using helps from Alara Dirik's tutorial: https://github.com/alaradirik/google-cloud-flask-tutorial [[deployment]](https://towardsdatascience.com/deploying-a-custom-ml-prediction-service-on-google-cloud-ae3be7e6d38f)


## How is it?
For the fare prediction, we are using only around 500.000 data from the whole dataset. And then we preprocess the dataset based on Dimitre Oliveira Data Preparation with some adjustments. And then we build the model using Keras model. At first we try to use Tensorflow Estimator, same with Dimitre's one. But then we make some adjustment so that we only use Keras in our project. After that, we do the training and export the saved model. Next, the saved model is deployed using Flask, with addition of Nginx and Unicorn, into Google Compute Engine. From that, we get the API that later will be triggered from the Android app in order to make some predictions.

## Step-by-Step
These are steps that you could do:
### Train the model
1. Download the dataset from Kaggle. [[dataset]](https://www.kaggle.com/c/new-york-city-taxi-fare-prediction)
2. Clone this repository
```bash
git clone https://github.com/anriza-fanani-bangkit/CAP0104-Capstone-Project/
```
3. Install the **requirements.txt**
```bash
pip install -r requirements.txt
```
4. Preprocess the **train.csv** and **test.csv** from dataset using **Data Preparation.ipynb** script to obtain the data for training.
5.  Train the data using **Model_Keras.ipynb** and get the saved model in Protocol Buffer (.pb) format or hdf5 (.h5) format. You can also convert them into json using TFJS converter and tflite model using tflite converter. You may also adjust the hyperparameter or the architecture.
### Model deployment using Flask
1. We need to check whether the model could be loaded or not using **load_model.ipynb**, if predictions could be done, then we are in the right way.
2. After that, we need to re-design the Model_Keras script so that it could be deployed in local web server using flask. Therefore we made **flask_model.py** for preprocessing and to try some predictions in localhost.
3. Next, we have two different scripts for flask deployment: **flask_using_dataform.ipynb** and **main.ipynb**. It is basically the same scripts, but with a little differences. In **flask_using_dataform.ipynb** we are using data form to make a prediction by uploading the CSV file and clik the predict button, and for **main.ipynb** we erased the data form in order to deploy it in Google Compute Engine so that the predictions could be done straight and continuosly. **main.py** basically the same script but with python format.
4. If you run **main.ipynb** script, you could try to send some POST requests using **requests.ipynb** or using POSTMAN service.
### FLask model deployment in Google Compute Engine
We are basically doing these things from Alara Dirik's tutorial above with some adjustment. The details could be read [[here]](https://towardsdatascience.com/deploying-a-custom-ml-prediction-service-on-google-cloud-ae3be7e6d38f)
1. First, you need to make a VM instance in Google Compute Engine (GCE). We are using Ubuntu 18.04 LTS and do not forget to allow the HTTP traffic.
2. Next we need to configure the firewall rules for Flask because it is running on port 5000 in VPC Network > Firewall Rules.
3. Then, we need to configure our VM instance. Open the VM using SSH.
4. After that, we will prepare the dependencies and the virtual environment. On the terminal run:
```bash
# update system packages, install the required packages, and miniconda
sudo apt-get update
sudo apt-get install bzip2 libxml2-dev libsm6 libxrender1 libfontconfig1
wget https://repo.anaconda.com/miniconda/Miniconda3-4.7.10-Linux-x86_64.sh
bash Miniconda3-4.7.10-Linux-x86_64.sh
# make conda executable
export PATH=/home/<based on your Google Cloud's name.miniconda3/bin:$PATH
rm Miniconda3-4.7.10-Linux-x86_64.sh
# create and activate a new virtual environment
conda create -n angkoot python=3.7
conda activate angkoot
```
5. Next, we will clone this repository to the VM. In order to make it easier, we have moved the **flask_model.py**, **keras_model.h5**, **main.py**, and **main_dataform.py** (additional) to the root folder of this repository. So, the root folder should look like this:
```
CAP0104-Capstone-Project
│   README.md
│   flask_model.py                  # Functions to preprocess the input data    
│   keras_model.h5                  # Keras saved model to be loaded in Flask
│   main.py                         # Flask model app to predict continuosly
│   main_dataform.py                # Flask model app to predict with data form
│   requirements.txt                # To install the dependencies
```              
6. Then, we need to install the dependencies from requirements.txt in the virtual environment and start running the **main.py** script:
```bash
cd CAP0104-Capstone-Project
pip install -r requirements.txt
python main.py
```
7. You can check it whether is it done right by sending some POST requests like before using VM external-ip address.
8. If it is done right, you could use Nginx and gunicorn to make the VM can handle HTTP requests, because Flask itself can not handle them. We need to install Nginx in GCE terminal.
```bash
# you need to go out from repo folder
cd
sudo apt-get install nginx-full
sudo /etc/init.d/nginx start
```
9. Next, we need to configure the Nginx.
```bash
# remove default configuration file
sudo rm /etc/nginx/sites-enabled/default
# create a new site configuration file
sudo touch /etc/nginx/sites-available/angkoot_project
sudo ln -s /etc/nginx/sites-available/angkoot_project /etc/nginx/sites-enabled/angkoot_project
```
10. Now, we may edit the configuration file.
```bash
sudo nano /etc/nginx/sites-enabled/angkoot_project
```
then, add this code below:
```bash
server {
    client_max_body_size 100M;
    location / {
        proxy_pass http://0.0.0.0:5000;
    }
}
```
11. Then, restart the Nginx server.
```bash
sudo /etc/init.d/nginx restart
```
12. Lastly, we need to go back to repo folder and bind the Flask model **main.py** to the Gunicorn server.
```bash
cd CAP0104-Capstone-Project
gunicorn --bind 0.0.0.0:5000 main:app
```
13. Now, we could trigger the API VM from anywhere. In our project, we may trigger it from our Android app.
