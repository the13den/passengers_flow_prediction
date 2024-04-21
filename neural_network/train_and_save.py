import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import MinMaxScaler
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense
import joblib

def load_and_transform_data(filepath):
    df = pd.read_excel(filepath)
    df.columns = ['Станция', 'Номер линии', 'Линия'] + [pd.to_datetime(date).strftime('%Y-%m-%d') for date in df.columns[3:]]
    df_melted = df.melt(id_vars=['Станция', 'Номер линии', 'Линия'], var_name='Дата', value_name='Загруженность')
    df_melted['Дата'] = pd.to_datetime(df_melted['Дата'])
    df_melted.sort_values(['Станция', 'Дата'], inplace=True)
    return df_melted

def train_models(data):
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
        model.save(f"/home/itech/Desktop/IT/mos metro/{station}_model.h5")  # Save model
        joblib.dump(scaler, f"/home/itech/Desktop/IT/mos metro/{station}_scaler.pkl")  # Save scaler


data = load_and_transform_data('/home/itech/Desktop/IT/mos metro/test.xlsx')

train_models(data)
