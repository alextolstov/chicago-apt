import convertPhoneNumber  from '../Login/Login'

describe('convertPhoneNumber', () => {
  it('works', () => {
    expect(convertPhoneNumber('12345')).toEqual('12345')
    expect(convertPhoneNumber(null)).toEqual('')
  })
})