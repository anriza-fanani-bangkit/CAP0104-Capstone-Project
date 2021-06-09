import tensorflow as tf
from tensorflow import keras
import pandas as pd
import numpy as np
from sklearn import preprocessing
from sklearn.model_selection import train_test_split

import warnings
warnings.filterwarnings('ignore')

def preprocess(filename):
    
    def process(df):
        df['pickup_longitude_binned'] = pd.qcut(df['pickup_longitude'], 16, labels=False)
        df['dropoff_longitude_binned'] = pd.qcut(df['dropoff_longitude'], 16, labels=False)
        df['pickup_latitude_binned'] = pd.qcut(df['pickup_latitude'], 16, labels=False)
        df['dropoff_latitude_binned'] = pd.qcut(df['dropoff_latitude'], 16, labels=False)

        df = df.drop('pickup_datetime', axis=1)
        return df

    def manhattan(pickup_lat, pickup_long, dropoff_lat, dropoff_long):
        return np.abs(dropoff_lat - pickup_lat) + np.abs(dropoff_long - pickup_long)
    
    def add_relevant_distances(df):
    # Add airpot distances and downtown
        ny = (-74.0063889, 40.7141667)
        jfk = (-73.7822222222, 40.6441666667)
        ewr = (-74.175, 40.69)
        lgr = (-73.87, 40.77)
        df['downtown_pickup_distance'] = manhattan(ny[1], ny[0], df['pickup_latitude'], df['pickup_longitude'])
        df['downtown_dropoff_distance'] = manhattan(ny[1], ny[0], df['dropoff_latitude'], df['dropoff_longitude'])
        df['jfk_pickup_distance'] = manhattan(jfk[1], jfk[0], df['pickup_latitude'], df['pickup_longitude'])
        df['jfk_dropoff_distance'] = manhattan(jfk[1], jfk[0], df['dropoff_latitude'], df['dropoff_longitude'])
        df['ewr_pickup_distance'] = manhattan(ewr[1], ewr[0], df['pickup_latitude'], df['pickup_longitude'])
        df['ewr_dropoff_distance'] = manhattan(ewr[1], ewr[0], df['dropoff_latitude'], df['dropoff_longitude'])
        df['lgr_pickup_distance'] = manhattan(lgr[1], lgr[0], df['pickup_latitude'], df['pickup_longitude'])
        df['lgr_dropoff_distance'] = manhattan(lgr[1], lgr[0], df['dropoff_latitude'], df['dropoff_longitude'])
        return df
    
    def add_engineered(df):
        lat1 = df['pickup_latitude']
        lat2 = df['dropoff_latitude']
        lon1 = df['pickup_longitude']
        lon2 = df['dropoff_longitude']
        latdiff = (lat1 - lat2)
        londiff = (lon1 - lon2)
        euclidean = (latdiff ** 2 + londiff ** 2) ** 0.5

        # Add new features
        df['latdiff'] = latdiff
        df['londiff'] = londiff
        df['euclidean'] = euclidean
        df['manhattan'] = manhattan(lat1, lon1, lat2, lon2)

        # One-hot encoding columns
        # Note, this is note the best way to one-hot encode features, but probably the simplest and will work here
        df = pd.get_dummies(df, columns=['weekday'])
        df = pd.get_dummies(df, columns=['month'])
        return df
    
    dtype_test = {'key': 'str',
             'pickup_datetime': 'str',
             'pickup_longitude': 'float32',
             'pickup_latitude': 'float32',
             'dropoff_longitude': 'float32',
             'dropoff_latitude': 'float32',
             'passenger_count': 'uint8',
             'year': 'uint16',
             'month': 'uint8',
             'day': 'uint8',
             'hour': 'uint8',
             'weekday': 'uint8',
             'night': 'uint8',
             'late_night': 'uint8'}

    dnames_test = ['key', 'pickup_datetime', 'pickup_longitude', 'pickup_latitude', 'dropoff_longitude', 'dropoff_latitude',
              'passenger_count', 'year', 'month', 'day', 'hour', 'weekday', 'night', 'late_night']

    dropped_columns = ['pickup_longitude', 'pickup_latitude',
                   'dropoff_longitude', 'dropoff_latitude']
    
    #data = pd.read_csv(filename, names=dnames_test, dtype=dtype_test, usecols=[0, 1, 2, 3, 4, 5, 7, 8, 9, 10, 11, 12, 13])
    data = process(filename)
    data = add_relevant_distances(data)
    data = add_engineered(data)
    data = data.drop(dropped_columns + ['key'], axis=1)
    scaler = preprocessing.MinMaxScaler()
    data = scaler.fit_transform(data).astype(np.float32)
    
    return data
    
def predict(data):
    prediction = model.predict(data)
    return prediction