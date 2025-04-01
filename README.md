# Oak GB
Oak GB is an emulator for the Gameboy and Gameboy Color, written in Java 8 and rendered using a Swing JFrame. The emulator is not cycle-accurate, but passes various important tests, including, cpu_instrs, dmg-acid2, and cgb-acid2

# Gallery
![Pokemon Gold Title Screen](https://github.com/tandr3w/Oak-GB/blob/main/resources/screenshot0.png)
![Pokemon Gold Battle Screen](https://github.com/tandr3w/Oak-GB/blob/main/resources/screenshot3.png)
![Tetris Game Screen](https://github.com/tandr3w/Oak-GB/blob/main/resources/screenshot8.png)
![Mario Title Screen](https://github.com/tandr3w/Oak-GB/blob/main/resources/screenshot10.png)
![Mario Game Screen](https://github.com/tandr3w/Oak-GB/blob/main/resources/screenshot13.png?raw=true)
![Zelda](https://github.com/tandr3w/Oak-GB/blob/main/resources/screenshot14.png?raw=true)

# Controls
| **Computer Keyboard**      | **Gameboy**                         |
|----------------------------|-------------------------------------|
| Z                          | A                                   |
| X                          | B                                   |
| Arrow Keys                 | Up/Down/Left/Right                  |
| Enter                      | Start                               |
| Space                      | Select                              |
| `                          | Take Screenshot                     |

# Usage
Run the ```build.sh``` script to create a runnable jar file, or ```run.sh``` to run the program. There are equivalent .bat files for windows machines. Then, simply select your ROM file to start the emulator.

# Features
- [x] Fully implemented CPU
- [x] Scanline-by-scanline renderer using Swing 
- [x] MBC1, MBC2, MBC3, and MBC5 mappers
- [x] Settings to change screen size & game speed
- [x] Support for .gb and .gbc files
- [ ] APU (coming soon)

# Created by:
- Andrew - [@tandr3w](github.com/tandr3w)
- Ailen - [@ayleri](github.com/ayleri)

