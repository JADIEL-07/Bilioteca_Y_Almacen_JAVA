from PIL import Image

def remove_white(input_path, output_path):
    img = Image.open(input_path).convert('RGBA')
    data = img.getdata()
    
    new_data = []
    for item in data:
        # Green is around (57, 169, 0).
        # White is (255,255,255).
        # Calculate how close the pixel is to white
        r, g, b, a = item
        # If it's mostly white/gray, make it transparent
        if r > 200 and g > 200 and b > 200:
            new_data.append((r, g, b, 0))
        else:
            new_data.append(item)
            
    img.putdata(new_data)
    img.save(output_path, 'PNG')

remove_white('C:/Users/Stiven/OneDrive/Desktop/Java/Bilioteca_Y_Almacen_JAVA/SENA_JAVA/src/main/resources/imagenes/icono_sena.png', 'C:/Users/Stiven/OneDrive/Desktop/Java/Bilioteca_Y_Almacen_JAVA/SENA_JAVA/src/main/resources/imagenes/logo_sena_trans.png')
