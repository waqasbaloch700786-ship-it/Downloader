import os
import requests
from urllib.parse import urlparse

def download_file(url, save_path="downloads"):
    try:
        os.makedirs(save_path, exist_ok=True)
        
        # file name nikalna
        parsed = urlparse(url)
        filename = os.path.basename(parsed.path)
        if not filename:
            filename = "downloaded_file"
        
        file_path = os.path.join(save_path, filename)
        
        # downloading with stream
        with requests.get(url, stream=True, timeout=30) as r:
            r.raise_for_status()
            total = int(r.headers.get('content-length', 0))
            
            with open(file_path, 'wb') as f:
                for chunk in r.iter_content(chunk_size=8192):
                    if chunk:
                        f.write(chunk)
        
        return {"status": "success", "path": file_path, "size": total}
    
    except Exception as e:
        return {"status": "failed", "error": str(e)}

if __name__ == "__main__":
    url = input("URL dalo: ")
    result = download_file(url)
    print(result)
