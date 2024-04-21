import numpy as np
from tensorflow.keras.models import load_model
import joblib
from datetime import datetime

def load_model_and_scaler(station_name):
    model = load_model(f"/home/itech/Desktop/IT/mos metro/{station_name}_model.h5")
    scaler = joblib.load(f"/home/itech/Desktop/IT/mos metro/{station_name}_scaler.pkl")
    return model, scaler

def predict_load(station_name, date_str):
    model, scaler = load_model_and_scaler(station_name)
    date = datetime.strptime(date_str, '%Y-%m-%d')
    timestamp = np.array([date.timestamp()]).reshape(-1, 1)
    timestamp_scaled = scaler.transform(timestamp)
    prediction = model.predict(timestamp_scaled)
    return {station_name: prediction.flatten()[0]}

def predict_all(date_str):
    predictions = {}
    import os
    for filename in os.listdir("/home/itech/Desktop/IT/mos metro/"):
        if filename.endswith("_model.h5"):
            station_name = filename.replace("_model.h5", "")
            predictions[station_name] = predict_load(station_name, date_str)[station_name]
    return predictions

# Example usage
station_name = 'Комсомольская'
date_to_predict = '2024-04-03'
predicted_load = predict_load(station_name, date_to_predict)
print(f'Predicted load for {station_name} on {date_to_predict}: {predicted_load}')

all_predictions = predict_all(date_to_predict)
print(f'Predicted loads for all stations on {date_to_predict}: {all_predictions}')
