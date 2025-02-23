# Cross Platform Peer-to-Peer File Transfer System (P2P Transfer)  

## Overview  
P2P Transfer is a **cross-platform** peer-to-peer file transfer system that allows users to **share files seamlessly across devices**. It uses **QR code-based authentication** and **network discovery** to enable secure file sharing over the same **WiFi network**. This project focuses on developing a cross-platform file transfer application that allows users to share files such as documents, photos,videos and any file formats seamlessly across mobile devices, desktops, and web platforms (Across OS - Windows, Linux, MacOS, Android) . Built using Android Native, the application ensures a consistent and responsive user experience across all platforms. Features like QR code scanning for device pairing and authetication,  and collaborative tools will enhance usability, making file sharing and teamwork more efficient.

## Demo
https://github.com/user-attachments/assets/f79e7968-c9f4-41c8-b2ac-738deddc8ebe
## Features  
- **Cross-Platform Support:**  
  - 🖥️ Windows (.exe)  
  - 📱 Android (Native App)  
  - 🐧 Linux (Web)  
  - 🍏 MacOS (Web)  
- **🔐 QR Code-Based Authentication:**  
  - ✅ Generates a **QR code** containing network credentials.  
  - 🔒 Ensures **secure authentication** before transferring files.  
- **📂 Local Network File Sharing:**  
  - 🔍 Discovers **devices on the same network** and ensures **Secure connectivity**
  - 🚀 Transfers files **without external internet dependency**.
     
- **🔧 Multi-Platform Compatibility:**  
  - 🐍 Uses **Python Flask** as the backend.  
  - 📱 Native implementations for **Android**.  
  - 🌐 Web-based access for **Linux and MacOS**.  
- **🔑 Security Features:**  
  - 📡 Payload contains **SSH and WiFi credentials** for seamless connectivity.  
  - 🔒 **Encrypted authentication** mechanism.  

## 📥 Installation :

### 🖥️ Windows (.exe) - Host Machine  
1. **Download** the latest `.exe` from the **WINDOWS EXE** directory.  
2. **Run** the executable.  
3. **Ctrl + Click** the IP address (either **localhost** or configured IP).  
4. **Upload and delete files** using the `.exe` application.  

### 🖥️ Windows (Sub-Machine)  
1. **Enter the host machine's IP address manually** in the browser.  
2. *(Future Update: Username and passkey authentication.)*  

### 📱 Android (Native App)  
1. **Install the APK** from **Android APK/P2P file transfer.apk**.  
2. **Open** the app and **grant necessary permissions**.  
3. **Tap the 3 dot button to Scan the QR code** from a Windows/Linux/Android Host machine.  
4. **Perform file transfer operations**.  

### 🐧 Linux & 🍏 MacOS (Web)  
1. **Clone** the repository:  
   ```sh
   git clone https://github.com/akranjithkumar/P2P-File-transfer
   ```
   ```sh
   cd P2P-File-transfer
   ```
2. **Install dependencies :**
   ```sh
   pip install -r requirements.txt ```

3. **Run the Flask server:**
   ```sh
    python app.py
    ```
4. **Scan the generated QR code to access the web interface.**

### 5. Usage:

### Start the Application
- **Run the executable** or **start the Flask server**.

### 📸 Scan the QR Code
- Use a **mobile or desktop application** to scan and connect to the **file-sharing network**.

### 📂 Upload and Download Files
- Use the **web interface** or **Android app** to upload/download files securely.

## 🛠️ Building the Executable

### 🖥️ For Windows (.exe)
```sh
pyinstaller --onefile --windowed app.py
```
### 🐧 For Linux/MacOS
```sh
python3 app.py
```

### For Android
Use Android Studio or Kotlin/Java Native Development

### 📜 License:
**This project is licensed under the Apache 2.0 License.**

### 🤝 Contribution

💡 Feel free to submit issues or contribute by forking the repository and making a pull request.
If any queries contact [dpijai@gmail.com]

🚀 Happy File Sharing! 🎉





   
 
