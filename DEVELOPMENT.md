# Development notes

## Useful commands for ssh keys

Since we need RSA keys in the pkcs8 format for the current JWT implementation:
Generate key pairs with `ssh-keygen -f id_rsa -e -m pem`
From this format you can then generate a correct public key representation:
`ssh-keygen -f id_rsa -e -m pkcs8 > id_rsa.pkcs8.pub`
and a correct private key representation:
`openssl pkcs8 -topk8 -in id_rsa -out id_rsa.pkcs8 -nocrypt`
