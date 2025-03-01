import { Cache } from './utils/cache'
import { Parser } from './parser/parser'

const test = async () => {
  try {
    const cache = new Cache()
    const parser = new Parser()
  } catch (error) {
    console.error(error)
  }
}

test()
