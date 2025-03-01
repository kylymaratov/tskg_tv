import { contextBridge, ipcRenderer } from 'electron'

export const backend = {
  nodeVersion: async (): Promise<string> =>
    await ipcRenderer.invoke('node-version'),
  homePage: async (): Promise<any> => await ipcRenderer.invoke('home-page'),
}

contextBridge.exposeInMainWorld('backend', backend)
