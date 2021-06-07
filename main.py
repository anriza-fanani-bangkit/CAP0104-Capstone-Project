from flask import Flask, make_response, request, render_template, jsonify
import io
from io import StringIO
import csv
import pandas as pd
import numpy as np
import os
from keras.models import load_model
from flask_model import preprocess

app = Flask(__name__)

@app.route('/')
def form():
    return """
        <html>
            <body>
                <h1>Fare Prediction</h1>
                </br>
                </br>
                <p> Insert your CSV file and then download the Result
                <form action="/transform" method="post" enctype="multipart/form-data">
                    <input type="file" name="data_file" class="btn btn-block"/>
                    </br>
                    </br>
                    <button type="submit" class="btn btn-primary btn-block btn-large">Predict</button>
                </form>
            </body>
        </html>
    """

@app.route('/transform', methods=["POST"])
def transform_view():
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
    
    # load the model from disk
    model = load_model('keras_model.h5')
    df = preprocess(data_test)
    prediction = model.predict(df)
    df_predict = pd.DataFrame(prediction, columns=["prediction"])
    df_predict.to_csv("prediction.csv", index=False, header=False, encoding='utf8')
    
    response = make_response(df_predict.to_csv())
    response.headers["Content-Disposition"] = "attachment; filename=result.csv"
    return jsonify(response)
    
if (__name__ == "__main__"):
     app.run(host="0.0.0.0", port = 5000, debug=False)