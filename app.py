from flask import Flask, request, send_from_directory, jsonify, render_template
from flask_cors import CORS
import os
import qrcode

app = Flask(__name__)
CORS(app)  # Enable CORS for all routes

# Directory for storing and serving files
UPLOAD_DIRECTORY = r"D:\fosshack\uploads"  # Change this to your desired directory

# Ensure the upload directory exists
os.makedirs(UPLOAD_DIRECTORY, exist_ok=True)

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

# Predefined text for QR Code
QR_TEXT = "192.168.0.1:5000"

# QR Code Directory
STATIC_DIR = "static"
QR_CODE_PATH = os.path.join(STATIC_DIR, "qrcode.png")

# Ensure static directory exists
os.makedirs(STATIC_DIR, exist_ok=True)

# Delete all files in static directory at startup
for filename in os.listdir(STATIC_DIR):
    file_path = os.path.join(STATIC_DIR, filename)
    try:
        if os.path.isfile(file_path):
            os.unlink(file_path)
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
    return send_from_directory(STATIC_DIR, "qrcode.png")

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)  # Accessible in the same network