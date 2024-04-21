import numpy as np
from tensorflow.keras.models import load_model
import joblib
from datetime import datetime
import json
import sys
import os

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
    return {station_name: int(prediction.flatten()[0])}  # Округляем предсказание для удобства

def predict_all(date_str):
    predictions = {}
    for filename in os.listdir("/home/itech/Desktop/IT/mos metro/"):
        if filename.endswith("_model.h5"):
            station_name = filename.replace("_model.h5", "")
            predictions[station_name] = predict_load(station_name, date_str)[station_name]
    return predictions

def predict_load_json(data):
    try:
        # Пытаемся интерпретировать входные данные как JSON
        request_details = json.loads(data)
        date_str = request_details.get('date')
        station_name = request_details.get('stationName')
        if station_name:
            prediction = predict_load(station_name, date_str)
        else:
            prediction = predict_all(date_str)
        return json.dumps(prediction, ensure_ascii=False)
    except json.JSONDecodeError:
        return json.dumps({"error": "Invalid JSON input"}, ensure_ascii=False)

if __name__ == "__main__":
    input_data = sys.argv[1]
    result = predict_load_json(input_data)
    print(result)
