from flask import Flask, request, send_from_directory, jsonify, render_template
from flask_cors import CORS
import os

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


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)  # Accessible in the same network
