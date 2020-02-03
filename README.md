# Build Helper
#### A mod for Minecraft intended as an introduction to Java.

## This is for FORGE v1.15.2

## Goals of this project:
1. Create a **Minecraft** mod that is useful and unique
2. Learn some **Java** intrinsic qualities and drawbacks
3. Produce the code in the *neatest way possible* (I'll keep refactoring)

## Where is the source?:
You will find the bulk of the source here:

#### [**main → java → com → edmistone → buildhelper**](https://github.com/aaronedmistone/build-helper/tree/forge-1.15.x/main/java/com/edmistone/buildhelper)

## Notes:
1. This mod is safe for survival and server use.
2. To allow for survival and server use of this mod, only players with `/op` can use the following:
- Symmetry Tool (because it currently does not calculate cost of symmetry, this will come soon though)
- Paste Variant Block (because I am not certain it cannot be abused for gathering valuable variants)
3. You can switch between any **Build Helper** blocks and items by `Shift + Right-Clicking`

## What are the features of the mod?
1. **Symmetry Tool:** Using this tool you can place lines of symmetry (in multiple forms) that then allow symmetrical building automatically with the given center and symmetry line/s.

2. **Copy Block:** By placing this block on opposing corners of a region, it will mark the region for pasting the region anywhere using one of the **Paste Blocks**. You can also rotate the region by clicking on the copy block with the **Build Tool**.

3. **Paste Block:** When placed this block will show the direction and area of where the copied blocks will be pasted. If you have placed 2 **Copy Blocks** on opposing corners of a region, you can then use the **Build Tool** on this block to paste the region.

4. **Paste Variant Block:** Exactly the same as a **Paste Block** but it will cycle through suitable known variants of blocks from the copied region when pasting.

5. **Build Tool:** This tool is simply used as a key to activating all of blocks in this mod. Switch between any tools/blocks by `Shift + Right-Clicking`


## What are the plans for the future?
This mod is intended as a learning experience and nothing more, I do intend to update the mod to the latest Minecraft Forge version and will likely keep it up to date so that others may learn from the mod source.
I also plan to keep adding functionality, configurability and stablility improvements, along with refactoring whenever I get free time.
