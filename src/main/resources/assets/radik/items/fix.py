import json
import os

paths = os.walk('.')

for path in paths:
    files = path[2]
    for file_name in files:
        if not file_name.endswith(".json"):
            continue
        with open(file_name, "r+", encoding="utf-8") as file:
            model = json.load(file)
            file.seek(0)
            if not "credit" in model and not "model" in model and not "elements" in model and not "textures" in model:
                print(f"Преобразуем {file_name}")
                model_parent = model["parent"]
                model = {}
                model["model"] = {
                    "type": "minecraft:model",
                    "model": model_parent
                }
                json.dump(model, file, indent=4)
                file.truncate()