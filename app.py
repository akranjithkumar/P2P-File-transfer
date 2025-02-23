from flask import Flask, request, send_from_directory, jsonify, render_template
from flask_cors import CORS
import os
import qrcode
import hashlib
import socket
import sys

app = Flask(__name__)
CORS(app)  # Enable CORS for all routes

# Determine the base directory for the application
if getattr(sys, 'frozen', False):
    BASE_DIR = sys._MEIPASS  # Temporary folder created by PyInstaller
else:
    BASE_DIR = os.path.dirname(os.path.abspath(__file__))

# Directories for storing and serving files
UPLOAD_DIRECTORY = os.path.join(BASE_DIR, "uploads")
STATIC_DIRECTORY = os.path.join(BASE_DIR, "static")

# Ensure the upload and static directories exist
os.makedirs(UPLOAD_DIRECTORY, exist_ok=True)
os.makedirs(STATIC_DIRECTORY, exist_ok=True)

# Function to get the local IP address
def get_local_ip():
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    try:
        s.connect(("8.8.8.8", 80))  # Connect to an external server
        ip = s.getsockname()[0]  # Get the assigned IP address
    except Exception:
        ip = "127.0.0.1"  # Fallback to localhost
    finally:
        s.close()
    return ip

# Hash function
def hash_ip(ip):
    return hashlib.sha256(ip.encode()).hexdigest()

hashed_ip = hash_ip(f"{get_local_ip()}:5000")

@app.route("/")
def home():
    return render_template("index.html")

@app.route("/files", methods=["GET"])
def list_files():
    try:
        files = os.listdir(UPLOAD_DIRECTORY)
        return jsonify(files)
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route("/download/<filename>", methods=["GET"])
def download_file(filename):
    try:
        file_path = os.path.join(UPLOAD_DIRECTORY, filename)
        if os.path.exists(file_path):
            return send_from_directory(UPLOAD_DIRECTORY, filename, as_attachment=True)
        return jsonify({"error": "File not found"}), 404
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route("/upload", methods=["POST"])
def upload_file():
    if "file" not in request.files:
        return jsonify({"error": "No file part"}), 400
    file = request.files["file"]
    if file.filename == "":
        return jsonify({"error": "No selected file"}), 400
    file.save(os.path.join(UPLOAD_DIRECTORY, file.filename))
    return jsonify({"message": "File uploaded successfully"})

@app.route("/delete/<filename>", methods=["DELETE"])
def delete_file(filename):
    try:
        file_path = os.path.join(UPLOAD_DIRECTORY, filename)
        if os.path.exists(file_path):
            os.remove(file_path)
            return jsonify({"message": f"'{filename}' has been deleted successfully"})
        return jsonify({"error": "File not found"}), 404
    except Exception as e:
        return jsonify({"error": str(e)}), 500

# Dynamic QR code based on IP address
QR_TEXT = "http://"+ f"{get_local_ip()}:5000"
QR_CODE_PATH = os.path.join(STATIC_DIRECTORY, "qrcode.png")

# Delete all files in static directory at startup
for filename in os.listdir(STATIC_DIRECTORY):
    file_path = os.path.join(STATIC_DIRECTORY, filename)
    try:
        if os.path.isfile(file_path):
            os.remove(file_path)  # Use os.remove for clarity
    except Exception as e:
        print(f"Error deleting {file_path}: {e}")

# Generate and save the QR code
def generate_qr():
    qr = qrcode.make(QR_TEXT)
    qr.save(QR_CODE_PATH)

generate_qr()  # Ensure QR is created at startup

@app.route("/qrcode")
def get_qr():
    generate_qr()
    return send_from_directory(STATIC_DIRECTORY, "qrcode.png")

@app.route("/decode", methods=["POST"])
def decode_qr():
    data = request.json.get("hash")
    if data == hashed_ip:
        return jsonify({"ip": get_local_ip(), "port": 5000})
    return jsonify({"error": "Invalid QR code"}), 400

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)  # Accessible in the same network