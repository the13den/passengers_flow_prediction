import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import MinMaxScaler
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense
from datetime import datetime

def load_and_transform_data(filepath):
    df = pd.read_excel(filepath)
    df.columns = ['Станция', 'Номер линии', 'Линия'] + [pd.to_datetime(date).strftime('%Y-%m-%d') for date in df.columns[3:]]
    df_melted = df.melt(id_vars=['Станция', 'Номер линии', 'Линия'], var_name='Дата', value_name='Загруженность')
    df_melted['Дата'] = pd.to_datetime(df_melted['Дата'])
    df_melted.sort_values(['Станция', 'Дата'], inplace=True)
    return df_melted

def train_models(data):
    models = {}
    scalers = {}
    for station, group in data.groupby('Станция'):
        timestamps = group['Дата'].values.astype('datetime64[s]').astype(np.int64) // 10**9
        loads = group['Загруженность'].values
        X_train, X_test, y_train, y_test = train_test_split(timestamps.reshape(-1, 1), loads, test_size=0.2, random_state=42)
        scaler = MinMaxScaler()
        X_train_scaled = scaler.fit_transform(X_train)
        X_test_scaled = scaler.transform(X_test)
        
        model = Sequential([
            Dense(128, activation='relu', input_shape=(1,)),
            Dense(128, activation='relu'),
            Dense(1, activation='linear')
        ])
        model.compile(optimizer='adam', loss='mean_squared_error')
        model.fit(X_train_scaled, y_train, epochs=10, batch_size=10, validation_data=(X_test_scaled, y_test))
        
        models[station] = model
        scalers[station] = scaler
    return models, scalers

def predict_load(models, scalers, station_name, date_str):
    date = datetime.strptime(date_str, '%Y-%m-%d')
    timestamp = np.array([date.timestamp()]).reshape(-1, 1)
    if station_name in models:
        model = models[station_name]
        scaler = scalers[station_name]
        timestamp_scaled = scaler.transform(timestamp)
        prediction = model.predict(timestamp_scaled)
        return {station_name: prediction.flatten()[0]}
    else:
        return {station_name: "Station not found"}

def predict_all(models, scalers, date_str):
    predictions = {}
    date = datetime.strptime(date_str, '%Y-%m-%d')
    timestamp = np.array([date.timestamp()]).reshape(-1, 1)
    for station, model in models.items():
        scaler = scalers[station]
        timestamp_scaled = scaler.transform(timestamp)
        prediction = model.predict(timestamp_scaled)
        predictions[station] = prediction.flatten()[0]
    return predictions

# Загрузка и преобразование данных
data = load_and_transform_data('/home/itech/Desktop/IT/mos metro/test.xlsx')

# Обучение моделей
models, scalers = train_models(data)

# Предсказание загруженности для одной станции на определённую дату
station_name = 'Комсомольская'
date_to_predict = '2024-04-03'
predicted_load = predict_load(models, scalers, station_name, date_to_predict)
print(f'Predicted load for {station_name} on {date_to_predict}: {predicted_load}')

# Предсказание загруженности всех станций на определённую дату
all_predictions = predict_all(models, scalers, date_to_predict)
print(f'Predicted loads for all stations on {date_to_predict}: {all_predictions}')
