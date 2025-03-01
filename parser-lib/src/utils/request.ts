import axios, { Method } from 'axios'
import { BASE_URL } from '../constants/constants'

interface RequestArgs {
  url: string
  method: Method
  headers?: any
  data?: any
}

const axiosExample = axios.create({ baseURL: BASE_URL })

export const request = async (
  args: RequestArgs
): Promise<string | undefined> => {
  try {
    const response = await axiosExample({ ...args })

    if (response.status !== 200) throw new Error('Bad status code responded')

    return response.data
  } catch (error) {
    console.log(error)
  }
}
