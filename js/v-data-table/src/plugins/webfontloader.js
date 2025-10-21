/**
 * plugins/webfontloader.js
 *
 * webfontloader documentation: https://github.com/typekit/webfontloader
 */

export async function loadFonts () {
  const webFontLoader = await import(/* webpackChunkName: "webfontloader" */'webfontloader')

  return new Promise((resolve) => {
    webFontLoader.load({
      google: {
        families: ['Roboto:100,300,400,500,700,900&display=swap'],
      },
      active: resolve,
      inactive: resolve,
    })
  })
}
