from flask import Flask, request, jsonify
from flask_cors import CORS
import subprocess
import json

app = Flask(__name__)
CORS(app)


@app.route('/stations/by-date', methods=['POST'])
def stations_by_date():
    if not request.json or 'time' not in request.json:
        return jsonify({"message": "Bad request", "error": "Missing 'time' in request"}), 400

    time = request.json['time']

    # Запуск скрипта нейросети с передачей времени
    try:
        # Оборачиваем время в JSON для универсальности
        input_data = json.dumps({'time': time})
        result = subprocess.run(['python', 'neural_network.py', input_data], capture_output=True, text=True, check=True)
        load_data = json.loads(result.stdout)
    except subprocess.CalledProcessError as e:
        return jsonify({"message": "Error executing neural network script", "error": str(e)}), 500
    except json.JSONDecodeError:
        return jsonify({"message": "Invalid JSON output from neural network script"}), 500

    # Конвертируем словарь загруженности в нужный формат JSON
    response = {
        "message": "Success",
        "stations": [{"stationName": key, "workload": value} for key, value in load_data.items()]
    }
    return jsonify(response)


@app.route('/stations/detailed', methods=['POST'])
def stations_detailed():
    if not request.json or not all(k in request.json for k in ['stationName', 'time']):
        return jsonify({"message": "Bad request", "error": "Missing required fields"}), 400

    input_data = json.dumps(request.json, ensure_ascii=False)

    # Запуск скрипта нейросети
    try:
        result = subprocess.run(['python', 'neural_network.py', input_data], capture_output=True, text=True, check=True)
        load_data = json.loads(result.stdout)
        station_name = request.json['stationName']
    except subprocess.CalledProcessError as e:
        return jsonify({"message": "Error executing neural network script", "error": str(e)}), 500
    except json.JSONDecodeError:
        return jsonify({"message": "Invalid JSON output from neural network script"}), 500

    # Отфильтровываем результат по запрошенной станции
    if station_name in load_data:
        station_data = load_data[station_name]
        response = {
            "message": "Success",
            "station": {
                "stationName": station_name,
                "workload": station_data
            }
        }
    else:
        return jsonify({"message": "Station not found"}), 404

    return jsonify(response)


if __name__ == '__main__':
    app.run(debug=True)
