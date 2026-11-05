import numpy as np
from PIL import Image

img = Image.open("static/img/montecristo-3.jpg").convert("RGB")
pixels = np.array(img).astype(float)

# Target colours
light = np.array([0xDB, 0xC7, 0x9F])  # #dbc79f
dark = np.array([0x2E, 0x26, 0x1E])  # #2e261e

# Calculate original pixel brightness (0 = black, 1 = white)
brightness = pixels.mean(axis=2) / 255.0

# Map brightness to target colours
result = (
    dark[None, None, :] * (1 - brightness[:, :, None])
    + light[None, None, :] * brightness[:, :, None]
)

# Save result
result = np.clip(result, 0, 255).astype(np.uint8)
Image.fromarray(result).save("monte-cristo-recoloured.png")

print("Done!")
