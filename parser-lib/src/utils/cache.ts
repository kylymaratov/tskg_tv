import * as path from 'path'
import * as fs from 'fs'

export class Cache {
  private cachePath = path.join(__dirname, '../../', 'caches')

  constructor() {
    const bool = fs.existsSync(this.cachePath)
    if (!bool) fs.mkdirSync(this.cachePath, { recursive: true })
  }

  async read(fileName: string) {
    return new Promise((resolve) => {
      fileName = this.cachePath + fileName
      fs.readFile(fileName, (err, data) => {
        if (err) console.log(`Failed read cache file: ${fileName}`)
        resolve(data.toJSON())
      })
    })
  }

  async write(fileName: string, data: any = {}) {
    return new Promise((resolve) => {
      fileName = this.cachePath + fileName
      fs.writeFile(fileName, data, (err) => {
        if (err) console.log(`Failed create cache file ${fileName}`)
        resolve(null)
      })
    })
  }

  async del(fileName: string) {
    return new Promise((resolve) => {
      fileName = this.cachePath + fileName
      fs.rm(fileName, (err) => {
        if (err) console.log(`Failed delete cache file ${fileName}`)
        resolve(null)
      })
    })
  }
}
