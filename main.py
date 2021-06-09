from flask import Flask, make_response, request
import io
from io import StringIO
import csv
import pandas as pd
import numpy as np
import os
from keras.models import load_model
from flask_model import preprocess

app = Flask(__name__)

@app.route("/")
def home():
    return "Kirimas was here"
    
@app.route("/predict", methods=["POST"])
def predict():
    if request.method == 'POST':
        f = request.files['data_file']
        if not f:
            return "No file"
        
    stream = io.StringIO(f.stream.read().decode("UTF8"), newline=None)
    csv_input = csv.reader(stream)
    stream.seek(0)
    result = stream.read()
    
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
    
    data_test = pd.read_csv(StringIO(result), names=dnames_test, dtype=dtype_test, usecols=[0, 1, 2, 3, 4, 5, 7, 8, 9, 10, 11, 12, 13])
    
    # load the model
    model = load_model('keras_model.h5')
    df = preprocess(data_test)
    prediction = model.predict(df)
    df_predict = pd.DataFrame(prediction, columns=["prediction"])
    response = df_predict.to_json()
    return response
    
if (__name__ == "__main__"):
     app.run(host='0.0.0.0/0', port = 5000, use_reloader=False, debug=False)
