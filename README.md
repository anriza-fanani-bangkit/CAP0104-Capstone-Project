# Angkoot Project by CAP0104 Capstone Project team Bangkit Academy 2021

## Overview
Angkoot is an app that has a purpose to digitalize Angkot Transportation in Surabaya, Indonesia. The services that Angkoot provides now are fare prediction for the order, trip distance estimation, and user registration.

## Credits
For the fare prediction we are inspired from Dimitre Oliveira:
https://github.com/dimitreOliveira/NewYorkCityTaxiFare

And the fare prediction dataset that we used is from Kaggle. [[dataset]](https://www.kaggle.com/c/new-york-city-taxi-fare-prediction)

The deployment process with Flask to Google Compute Engine has been done using helps from Alara Dirk's tutorial: https://github.com/alaradirik/google-cloud-flask-tutorial [[deployment]](https://towardsdatascience.com/deploying-a-custom-ml-prediction-service-on-google-cloud-ae3be7e6d38f)


## How is it?
For the fare prediction, we are using only around 500.000 data from the whole dataset. And then we preprocess the dataset based on Dimitre Oliveira Data Preparation with some adjustments. And then we build the model using Keras model. At first we try to use Tensorflow Estimator, same with Dimitre's one. But then we make some adjustment so that we only use Keras in our project. After that, we do the training and export the saved model. Next, the saved model is deployed using Flask, with addition of Nginx and Unicorn, into Google Compute Engine. From that, we get the API that later will be triggered from the Android app in order to make some predictions.

## Step-by-Step
These are steps that you could do:
### Train the model
1. Download the dataset from Kaggle
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
