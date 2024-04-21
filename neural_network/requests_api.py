import requests
import json


base_url = "http://127.0.0.1:5000"

# Данные для запроса /stations/by-date
data_by_date = {
    "time": "1985-09-25 17:45:30.005"
}

# Данные для запроса /stations/detailed
data_detailed = {
    "stationName": "Комсомольская",
    "time": "1985-09-25 17:45:30.005"
}

# Отправка запроса на /stations/by-date
response_by_date = requests.post(f"{base_url}/stations/by-date", json=data_by_date)
print("Response from /stations/by-date:")
print(json.dumps(response_by_date.json(), indent=4,ensure_ascii=False))

# Отправка запроса на /stations/detailed
response_detailed = requests.post(f"{base_url}/stations/detailed", json=data_detailed)
print("Response from /stations/detailed:")
print(json.dumps(response_detailed.json(), indent=4,ensure_ascii=False))
